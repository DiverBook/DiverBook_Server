package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.entity.RefreshToken;
import ada.divercity.diverbook_server.repository.RefreshTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void storeRefreshToken(UUID userId, String token) {
        refreshTokenRepository.deleteByUserId(userId);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(token)
                .build();
        refreshTokenRepository.save(refreshToken);
    }
}
