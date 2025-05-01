package ada.divercity.diverbook_server.repository;

import ada.divercity.diverbook_server.entity.User;
import ada.divercity.diverbook_server.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Integer> {

    boolean existsByUserIdAndBadgeCode(UUID userId, String badgeCode);

    List<UserBadge> findByUser(User user);

}
