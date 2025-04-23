package ada.divercity.diverbook_server.controller;

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

//    @PostMapping
//    public ResponseEntity<UserDto> createUser(@RequestBody RegisterUserRequest request) {
//        UserDto dto = userService.createUser(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
//    }

    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(
            @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal UUID userId
    ) {
        UserDto updated = userService.updateUser(userId, request);
        return ResponseEntity.ok(updated);
    }
}
