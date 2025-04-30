package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.UserBadgeDto;
import ada.divercity.diverbook_server.entity.Badge;
import ada.divercity.diverbook_server.entity.User;
import ada.divercity.diverbook_server.entity.UserBadge;
import ada.divercity.diverbook_server.repository.BadgeRepository;
import ada.divercity.diverbook_server.repository.UserBadgeRepository;
import ada.divercity.diverbook_server.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserBadgeServiceImpl implements UserBadgeService {

    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    public List<UserBadgeDto> getUserBadgesByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Owner User not found"));

        List<UserBadge> userBadges = userBadgeRepository.findByUser(user);

        return userBadges.stream()
                .map(UserBadgeDto::fromEntity)
                .toList();
    }

//    @Transactional
    public UserBadgeDto createUserBadge(UUID userId, String badgeCode) {
        if (userBadgeRepository.existsByUserIdAndBadgeCode(userId, badgeCode)) {
            throw new RuntimeException("User badge already exists for this user and badge code");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Badge badge = badgeRepository.findByCode(badgeCode)
                .orElseThrow(() -> new RuntimeException("Badge not found"));

        UserBadge userBadge = UserBadge.builder()
                .user(user)
                .badge(badge)
                .acquiredDate(LocalDateTime.now())
                .build();

        UserBadge savedUserBadge = userBadgeRepository.save(userBadge);

        return UserBadgeDto.fromEntity(savedUserBadge);
    }
}
