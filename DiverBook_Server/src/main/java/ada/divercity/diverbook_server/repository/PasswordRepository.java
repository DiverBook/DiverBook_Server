package ada.divercity.diverbook_server.repository;

import ada.divercity.diverbook_server.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasswordRepository extends JpaRepository<Password, UUID> {
    void deleteByUserId(UUID userId);
}
