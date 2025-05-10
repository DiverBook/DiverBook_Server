package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.BadgeDto;
import ada.divercity.diverbook_server.entity.Badge;
import ada.divercity.diverbook_server.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {
    private final BadgeRepository badgeRepository;

    public List<BadgeDto> getAllBadges() {
        List<Badge> badges = badgeRepository.findAll();

        return badges.stream()
                .sorted((badge1, badge2) -> badge1.getCode().compareTo(badge2.getCode()))
                .map(BadgeDto::fromEntity)
                .toList();
    }
}
