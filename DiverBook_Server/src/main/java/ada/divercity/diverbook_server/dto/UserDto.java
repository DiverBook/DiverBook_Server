package ada.divercity.diverbook_server.dto;

import ada.divercity.diverbook_server.entity.User;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String userName;
    private String divisions;
    private String phoneNumber;
    private String interests;
    private String places;
    private String about;

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .divisions(user.getDivisions())
                .phoneNumber(user.getPhoneNumber())
                .interests(user.getInterests())
                .places(user.getPlaces())
                .about(user.getAbout())
                .build();
    }
}
