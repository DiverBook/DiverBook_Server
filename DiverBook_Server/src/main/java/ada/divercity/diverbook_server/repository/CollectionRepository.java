package ada.divercity.diverbook_server.repository;

import ada.divercity.diverbook_server.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    boolean existsByOwnerIdAndFoundUserId(UUID ownerId, UUID foundUserId);
}
