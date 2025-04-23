package ada.divercity.diverbook_server.service;

import java.util.UUID;

public interface RefreshTokenService {
    public void storeRefreshToken(UUID userId, String token);
}
