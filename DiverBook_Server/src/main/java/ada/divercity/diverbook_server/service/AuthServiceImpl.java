package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.AuthRequest;
import ada.divercity.diverbook_server.dto.AuthResponse;
import ada.divercity.diverbook_server.dto.RegisterUserRequest;
import ada.divercity.diverbook_server.dto.UserDto;
import ada.divercity.diverbook_server.entity.TokenBlackList;
import ada.divercity.diverbook_server.entity.User;
import ada.divercity.diverbook_server.repository.TokenBlackListRepository;
import ada.divercity.diverbook_server.repository.UserRepository;
import ada.divercity.diverbook_server.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlackListRepository tokenBlackListRepository;

    public AuthResponse registerAndLogin(RegisterUserRequest request) {
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        UserDto newUserDto = userService.createUser(request);

        String accessToken = jwtTokenProvider.generateAccessToken(newUserDto.getId().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(newUserDto.getId().toString());

        return new AuthResponse(newUserDto.getId(), accessToken, refreshToken);
    }

    public AuthResponse reissueAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token is expired or invalid");
        }

        String userId = jwtTokenProvider.validateAndGetUserId(refreshToken); // 유효성 검사 + userId 추출

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);

        return new AuthResponse(UUID.fromString(userId), newAccessToken, newRefreshToken);
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId().toString());

        return new AuthResponse(user.getId(), accessToken, refreshToken);
    }

    public AuthResponse logout(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token is expired or invalid");
        }

        String userId = jwtTokenProvider.validateAndGetUserId(refreshToken);
        tokenBlackListRepository.save(TokenBlackList.builder().token(refreshToken).build());

        return new AuthResponse(UUID.fromString(userId), null, null);
    }
}
