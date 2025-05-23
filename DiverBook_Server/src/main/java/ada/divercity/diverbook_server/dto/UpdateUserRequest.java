package ada.divercity.diverbook_server.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private String userName;
    private String divisions;
    private String phoneNumber;
    private String interests;
    private String places;
    private String about;
}
