package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.AuthRequest;
import ada.divercity.diverbook_server.dto.AuthResponse;
import ada.divercity.diverbook_server.dto.RegisterUserRequest;
import ada.divercity.diverbook_server.dto.UserDto;
import ada.divercity.diverbook_server.entity.RefreshToken;
import ada.divercity.diverbook_server.entity.User;
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
    private final RefreshTokenService refreshTokenService;

    public AuthResponse registerAndLogin(RegisterUserRequest request) {
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        UserDto newUserDto = userService.createUser(request);

        String accessToken = jwtTokenProvider.generateAccessToken(newUserDto.getId().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(newUserDto.getId().toString());

        refreshTokenService.storeRefreshToken(newUserDto.getId(), refreshToken);

        return new AuthResponse(newUserDto.getId(), accessToken, refreshToken);
    }


}
