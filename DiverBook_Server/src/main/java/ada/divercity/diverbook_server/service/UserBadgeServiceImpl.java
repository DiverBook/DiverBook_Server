package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.UserBadgeDto;
import ada.divercity.diverbook_server.entity.Badge;
import ada.divercity.diverbook_server.entity.User;
import ada.divercity.diverbook_server.entity.UserBadge;
import ada.divercity.diverbook_server.exception.CustomException;
import ada.divercity.diverbook_server.exception.ErrorCode;
import ada.divercity.diverbook_server.repository.BadgeRepository;
import ada.divercity.diverbook_server.repository.UserBadgeRepository;
import ada.divercity.diverbook_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserBadgeServiceImpl implements UserBadgeService {

    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;

    public List<UserBadgeDto> getUserBadgesByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<UserBadge> userBadges = userBadgeRepository.findByUser(user);

        return userBadges.stream()
                .map(UserBadgeDto::fromEntity)
                .toList();
    }

    public UserBadgeDto createUserBadge(UUID userId, String badgeCode) {
        if (userBadgeRepository.existsByUserIdAndBadgeCode(userId, badgeCode)) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_BADGE);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Badge badge = badgeRepository.findByCode(badgeCode)
                .orElseThrow(() -> new CustomException(ErrorCode.BADGE_NOT_FOUND));

        UserBadge userBadge = UserBadge.builder()
                .user(user)
                .badge(badge)
                .acquiredDate(LocalDateTime.now())
                .build();

        UserBadge savedUserBadge = userBadgeRepository.save(userBadge);

        return UserBadgeDto.fromEntity(savedUserBadge);
    }
}
