package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.AuthResponse;
import ada.divercity.diverbook_server.dto.RegisterUserRequest;


public interface AuthService {
    public AuthResponse registerAndLogin(RegisterUserRequest request);
}
