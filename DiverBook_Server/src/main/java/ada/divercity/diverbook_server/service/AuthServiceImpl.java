package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.AuthRequest;
import ada.divercity.diverbook_server.dto.AuthResponse;
import ada.divercity.diverbook_server.dto.RegisterUserRequest;
import ada.divercity.diverbook_server.dto.UserDto;
import ada.divercity.diverbook_server.entity.Password;
import ada.divercity.diverbook_server.entity.TokenBlackList;
import ada.divercity.diverbook_server.entity.User;
import ada.divercity.diverbook_server.repository.PasswordRepository;
import ada.divercity.diverbook_server.repository.TokenBlackListRepository;
import ada.divercity.diverbook_server.repository.UserRepository;
import ada.divercity.diverbook_server.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordRepository passwordRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlackListRepository tokenBlackListRepository;


    @Transactional
    public AuthResponse activateAndLogin(RegisterUserRequest request) {
        Optional<User> user = userRepository.findByUserName(request.getUserName());
        if (user.isEmpty()) {
            throw new RuntimeException("User is not exists");
        }

        if (user.get().getIsActivated()) {
            throw new RuntimeException("User is already activated");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new RuntimeException("Password cannot be null or empty");
        }

        UserDto newUserDto = userService.activateUser(request);
        Boolean isAdded = userService.addNewPassword(user.get().getId(), request.getPassword());
        if (!isAdded) {
            userService.deactivateUser(user.get().getId());
            throw new RuntimeException("Password cannot be added");
        }

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
        Password password = passwordRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!new BCryptPasswordEncoder().matches(request.getPassword(), password.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(password.getUserId().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(password.getUserId().toString());

        return new AuthResponse(password.getUserId(), accessToken, refreshToken);
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
