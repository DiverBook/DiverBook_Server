package ada.divercity.diverbook_server.controller;

import ada.divercity.diverbook_server.dto.ChangePasswordRequest;
import ada.divercity.diverbook_server.dto.ProfileImageResponse;
import ada.divercity.diverbook_server.dto.UpdateUserRequest;
import ada.divercity.diverbook_server.dto.UserDto;
import ada.divercity.diverbook_server.entity.ProfileImage;
import ada.divercity.diverbook_server.service.UserProfileImageServiceImpl;
import ada.divercity.diverbook_server.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class UserController {

    private final UserServiceImpl userService;
    private final UserProfileImageServiceImpl userProfileImageService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(@AuthenticationPrincipal UUID userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/{id}/profile-image")
    public ResponseEntity<ProfileImageResponse> getUserProfileImage(@PathVariable UUID id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(userProfileImageService.getProfileImageByUserName(user.getUserName()));
    }

    @GetMapping("/{id}/achievement-rate")
    public ResponseEntity<Float> getAchievementRate(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getAchievementRateById(id));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(
            @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal UUID userId
    ) {
        UserDto updated = userService.updateUser(userId, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Boolean> changeUserPassword(
            @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UUID userId
    ) {
        Boolean updated = userService.changeUserPassword(userId, request);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/me/deactivate")
    public ResponseEntity<UserDto> deactivateUser(
            @AuthenticationPrincipal UUID userId
    ) {
        UserDto updated = userService.deactivateUser(userId);
        return ResponseEntity.ok(updated);
    }
}
