package ada.divercity.diverbook_server.controller;

import ada.divercity.diverbook_server.dto.*;
import ada.divercity.diverbook_server.service.AuthService;
import ada.divercity.diverbook_server.service.TokenBlackListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        AuthResponse response = authService.activateAndLogin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        if (request.getRefreshToken() == null || request.getRefreshToken().trim().isEmpty()) {
            throw new IllegalArgumentException("Refresh token cannot be null or empty");
        }
        AuthResponse tokens = authService.reissueAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(tokens));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse tokens = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(tokens));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<AuthResponse>> logout(@Valid @RequestBody LogoutRequest request) {
        AuthResponse response = authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}