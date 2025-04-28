package ada.divercity.diverbook_server.controller;

import ada.divercity.diverbook_server.dto.UserBadgeDto;
import ada.divercity.diverbook_server.entity.UserBadge;
import ada.divercity.diverbook_server.service.UserBadgeServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-badge")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class UserBadgeController {

    private final UserBadgeServiceImpl userBadgeService;

    @GetMapping
    public ResponseEntity<List<UserBadgeDto>> getUserBadges(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        List<UserBadgeDto> badges = userBadgeService.getUserBadgesByUserId(userId);

        return ResponseEntity.ok(badges);
    }

    @PostMapping("/{badgeCode}")
    public ResponseEntity<UserBadgeDto> createUserBadge(
            @PathVariable String badgeCode,
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        UserBadgeDto userBadge = userBadgeService.createUserBadge(userId, badgeCode);

        return ResponseEntity.ok(userBadge);
    }
}
