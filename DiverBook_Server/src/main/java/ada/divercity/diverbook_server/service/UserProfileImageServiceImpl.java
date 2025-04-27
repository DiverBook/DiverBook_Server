package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.ProfileImageResponse;
import ada.divercity.diverbook_server.entity.ProfileImage;
import ada.divercity.diverbook_server.repository.ProfileImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileImageServiceImpl implements UserProfileImageService {

    private final ProfileImageRepository userProfileImageRepository;
    public ProfileImageResponse getProfileImageByUserName(String username) {
        ProfileImage profileImage = userProfileImageRepository.findByUserName(username);
        if (profileImage == null) {
            return null;
        }
        ProfileImageResponse response = new ProfileImageResponse();
        response.setUserImage(profileImage.getUserImage());
        return response;
    }
}
