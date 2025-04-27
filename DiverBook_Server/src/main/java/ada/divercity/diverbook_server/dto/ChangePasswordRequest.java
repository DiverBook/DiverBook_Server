package ada.divercity.diverbook_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String password;
    private String newPassword;
    private String refreshToken;
}
