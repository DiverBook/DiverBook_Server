package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.UserBadgeDto;

import java.util.List;
import java.util.UUID;

public interface UserBadgeService {
    public List<UserBadgeDto> getUserBadgesByUserId(UUID userId);

    public UserBadgeDto createUserBadge(UUID userId, String badgeCode);
}
