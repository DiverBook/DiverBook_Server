package ada.divercity.diverbook_server.controller;

import ada.divercity.diverbook_server.dto.*;
import ada.divercity.diverbook_server.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @SecurityRequirement(name = "JWT")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getMe(@AuthenticationPrincipal UUID userId) {
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable UUID id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}/achievement-rate")
    public ResponseEntity<ApiResponse<Float>> getAchievementRate(@PathVariable UUID id) {
        Float achievementRate = userService.getAchievementRateById(id);
        return ResponseEntity.ok(ApiResponse.success(achievementRate));
    }

    @SecurityRequirement(name = "JWT")
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal UUID userId
    ) {
        UserDto updated = userService.updateUser(userId, request);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @SecurityRequirement(name = "JWT")
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<String>> changeUserPassword(
            @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UUID userId
    ) {
        String refreshToken = userService.changeUserPassword(userId, request);
        return ResponseEntity.ok(ApiResponse.success(refreshToken));
    }

    @SecurityRequirement(name = "JWT")
    @PostMapping("/me/deactivate")
    public ResponseEntity<ApiResponse<UserDto>> deactivateUser(
            @AuthenticationPrincipal UUID userId
    ) {
        UserDto updated = userService.deactivateUser(userId);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @GetMapping("/activation/{name}")
    public ResponseEntity<ApiResponse<Boolean>> getActivationStatus(
            @PathVariable String name
    ) {
        Boolean activationStatus = userService.getUserActivationStatusByName(name);
        return ResponseEntity.ok(ApiResponse.success(activationStatus));
    }
}
