package ada.divercity.diverbook_server.dto;

import ada.divercity.diverbook_server.entity.Badge;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeDto {
    private String code;
    private String name;
    private String description;
    private String imageUrl;

    public static BadgeDto fromEntity(Badge badge) {
        return BadgeDto.builder()
                .code(badge.getCode())
                .name(badge.getName())
                .description(badge.getDescription())
                .imageUrl(badge.getImageUrl())
                .build();
    }
}
