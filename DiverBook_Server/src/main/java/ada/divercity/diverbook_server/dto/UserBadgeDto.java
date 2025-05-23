package ada.divercity.diverbook_server.dto;

import ada.divercity.diverbook_server.entity.Badge;
import ada.divercity.diverbook_server.entity.User;
import ada.divercity.diverbook_server.entity.UserBadge;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBadgeDto {
    private Integer id;
    private User user;
    private Badge badge;
    private LocalDateTime acquiredDate;

    static public UserBadgeDto fromEntity(UserBadge userBadge) {
        return UserBadgeDto.builder()
                .id(userBadge.getId())
                .user(userBadge.getUser())
                .badge(userBadge.getBadge())
                .acquiredDate(userBadge.getAcquiredDate())
                .build();
    }
}
