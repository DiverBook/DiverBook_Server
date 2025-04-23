package ada.divercity.diverbook_server.controller;

import ada.divercity.diverbook_server.dto.AuthRequest;
import ada.divercity.diverbook_server.dto.AuthResponse;
import ada.divercity.diverbook_server.dto.RegisterUserRequest;
import ada.divercity.diverbook_server.dto.TokenRefreshRequest;
import ada.divercity.diverbook_server.service.AuthService;
import ada.divercity.diverbook_server.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenBlackListService tokenBlackListService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        System.out.println(request);
        AuthResponse response = authService.registerAndLogin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        if (request.getRefreshToken() == null || request.getRefreshToken().trim().isEmpty()) {
            throw new IllegalArgumentException("Refresh token cannot be null or empty");
        }
        AuthResponse tokens = authService.reissueAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse tokens = authService.login(request);
        return ResponseEntity.ok(tokens);
    }
}