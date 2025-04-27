package ada.divercity.diverbook_server.repository;

import ada.divercity.diverbook_server.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    ProfileImage findByUserName(String userName);
}
