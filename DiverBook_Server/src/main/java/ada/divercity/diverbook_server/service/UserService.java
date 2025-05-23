package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public UserDto createUser(RegisterUserRequest request);

    public UserDto activateUser(RegisterUserRequest request);

    public UserDto deactivateUser(UUID id, DeactivateRequest request);

    public Boolean getUserActivationStatusByName(String userName);

    public UserDto getUserById(UUID id);

    public List<UserDto> getAllUsers();

    public String getProfileImageUrlByName(String userName);

    public Float getAchievementRateById(UUID id);

    public UserDto updateUser(UUID id, UpdateUserRequest request);

    public Boolean addNewPassword(UUID id, String rawPassword);

    public String changeUserPassword(UUID id, ChangePasswordRequest request);

    private String encodePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        return new BCryptPasswordEncoder().encode(rawPassword);
    }
}
