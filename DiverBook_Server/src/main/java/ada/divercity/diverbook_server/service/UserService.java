package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.RegisterUserRequest;
import ada.divercity.diverbook_server.dto.ChangePasswordRequest;
import ada.divercity.diverbook_server.dto.UpdateUserRequest;
import ada.divercity.diverbook_server.dto.UserDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public interface UserService {
    public UserDto createUser(RegisterUserRequest request);

    public UserDto activateUser(RegisterUserRequest request);

    public UserDto deactivateUser(UUID id);

    public UserDto getUserById(UUID id);

    public Float getAchievementRateById(UUID id);

    public UserDto updateUser(UUID id, UpdateUserRequest request);

    public Boolean addNewPassword(UUID id, String rawPassword);

    public Boolean changeUserPassword(UUID id, ChangePasswordRequest request);

    private String encodePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        return new BCryptPasswordEncoder().encode(rawPassword);
    }
}
