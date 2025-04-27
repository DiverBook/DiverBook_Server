package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.ProfileImageResponse;

public interface UserProfileImageService {
    public ProfileImageResponse getProfileImageByUserName(String username);
}
