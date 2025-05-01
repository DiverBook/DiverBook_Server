package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.AuthRequest;
import ada.divercity.diverbook_server.dto.AuthResponse;
import ada.divercity.diverbook_server.dto.RegisterUserRequest;
import ada.divercity.diverbook_server.dto.UserDto;
import ada.divercity.diverbook_server.entity.Password;
import ada.divercity.diverbook_server.entity.TokenBlackList;
import ada.divercity.diverbook_server.entity.User;
import ada.divercity.diverbook_server.exception.CustomException;
import ada.divercity.diverbook_server.exception.ErrorCode;
import ada.divercity.diverbook_server.repository.PasswordRepository;
import ada.divercity.diverbook_server.repository.TokenBlackListRepository;
import ada.divercity.diverbook_server.repository.UserRepository;
import ada.divercity.diverbook_server.security.JwtTokenProvider;
import ada.divercity.diverbook_server.security.TokenValidationStatus;
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
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        if (user.get().getIsActivated()) {
            throw new CustomException(ErrorCode.USER_ALREADY_ACTIVATED);
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new CustomException(ErrorCode.MISSING_REQUIRED_FIELD);
        }

        UserDto newUserDto = userService.activateUser(request);
        Boolean isAdded = userService.addNewPassword(user.get().getId(), request.getPassword());
        if (!isAdded) {
            userService.deactivateUser(user.get().getId());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(newUserDto.getId().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(newUserDto.getId().toString());

        return new AuthResponse(newUserDto.getId(), accessToken, refreshToken);
    }

    public AuthResponse reissueAccessToken(String refreshToken) {
        try {
            String userId = jwtTokenProvider.validateAndGetUserId(refreshToken);

            String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);

            return new AuthResponse(UUID.fromString(userId), newAccessToken, newRefreshToken);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public AuthResponse login(AuthRequest request) {
        Password password = passwordRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PASSWORD_NOT_FOUND));

        if (!new BCryptPasswordEncoder().matches(request.getPassword(), password.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(password.getUserId().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(password.getUserId().toString());

        return new AuthResponse(password.getUserId(), accessToken, refreshToken);
    }

    public AuthResponse logout(String refreshToken) {
        try {
            String userId = jwtTokenProvider.validateAndGetUserId(refreshToken);

            tokenBlackListRepository.save(TokenBlackList.builder().token(refreshToken).build());

            return new AuthResponse(UUID.fromString(userId), null, null);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

}
