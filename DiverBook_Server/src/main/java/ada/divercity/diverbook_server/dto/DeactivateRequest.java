package ada.divercity.diverbook_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeactivateRequest {
    private String refreshToken;
}
