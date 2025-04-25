package ada.divercity.diverbook_server.controller;

import ada.divercity.diverbook_server.dto.ChangePasswordRequest;
import ada.divercity.diverbook_server.dto.UpdateUserRequest;
import ada.divercity.diverbook_server.dto.UserDto;
import ada.divercity.diverbook_server.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe(@AuthenticationPrincipal UUID userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
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
}
