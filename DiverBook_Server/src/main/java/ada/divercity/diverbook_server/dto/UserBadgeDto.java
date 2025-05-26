package ada.divercity.diverbook_server.dto;

import ada.divercity.diverbook_server.entity.UserBadge;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBadgeDto {
    private Integer id;
    private UUID userId;
    private String badgeCode;
    private LocalDateTime acquiredDate;

    public static UserBadgeDto fromEntity(UserBadge userBadge) {
        return UserBadgeDto.builder()
                .id(userBadge.getId())
                .userId(userBadge.getUser().getId())
                .badgeCode(userBadge.getBadge().getCode())
                .acquiredDate(userBadge.getAcquiredDate())
                .build();
    }
}
