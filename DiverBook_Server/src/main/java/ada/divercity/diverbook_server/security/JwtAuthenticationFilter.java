package ada.divercity.diverbook_server.security;

import ada.divercity.diverbook_server.exception.CustomException;
import ada.divercity.diverbook_server.exception.ErrorCode;
import ada.divercity.diverbook_server.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            System.out.println(request.getHeader("Authorization"));
            try {
                String userId = jwtTokenProvider.validateAccessTokenAndGetUserId(token);
                if (userId != null) {
                    // Convert the userId string to UUID
                    UUID userUuid = UUID.fromString(userId);
                    // Verify the user exists but use the UUID as the principal
                    userRepository.findById(userUuid)
                            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userUuid, null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                if (e instanceof CustomException) {
                    if (((CustomException) e).getErrorCode() == ErrorCode.EXPIRED_TOKEN) {
                        System.out.println("Expired JWT token");
                        throw new CustomException(ErrorCode.EXPIRED_TOKEN);
                    } else if (((CustomException) e).getErrorCode() == ErrorCode.INVALID_TOKEN) {
                        System.out.println("Invalid JWT token");
                        throw new CustomException(ErrorCode.INVALID_TOKEN);
                    }
                } else {
                    throw e;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}