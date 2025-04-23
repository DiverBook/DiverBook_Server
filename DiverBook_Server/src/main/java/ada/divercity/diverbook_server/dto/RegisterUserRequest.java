package ada.divercity.diverbook_server.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserRequest {
    private UUID id;
    private String userName;
    private String userImage;
    private String divisions;
    private String phoneNumber;
    private String interests;
    private String places;
    private String about;

    private String password;
}
