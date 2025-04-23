package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.AuthRequest;
import ada.divercity.diverbook_server.dto.AuthResponse;
import ada.divercity.diverbook_server.dto.RegisterUserRequest;


public interface AuthService {
    public AuthResponse registerAndLogin(RegisterUserRequest request);
    public AuthResponse reissueAccessToken(String refreshToken);
    public AuthResponse login(AuthRequest request);
    public AuthResponse logout(String token);
}
