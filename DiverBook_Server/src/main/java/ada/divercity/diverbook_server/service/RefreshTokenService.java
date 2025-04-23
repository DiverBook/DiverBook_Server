package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.entity.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {
    public void storeRefreshToken(UUID userId, String token);
    public Optional<RefreshToken> findByToken(String token);
    public void deleteByUserId(UUID userId);
}
