package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.RegisterUserRequest;
import ada.divercity.diverbook_server.dto.ChangePasswordRequest;
import ada.divercity.diverbook_server.dto.UpdateUserRequest;
import ada.divercity.diverbook_server.dto.UserDto;
import ada.divercity.diverbook_server.entity.Password;
import ada.divercity.diverbook_server.entity.User;
import ada.divercity.diverbook_server.repository.PasswordRepository;
import ada.divercity.diverbook_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;

    public UserDto createUser(RegisterUserRequest request) {
        if (userRepository.findById(request.getId()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .id(request.getId())
                .userName(request.getUserName())
                .userImage(request.getUserImage())
                .divisions(request.getDivisions())
                .phoneNumber(request.getPhoneNumber())
                .interests(request.getInterests())
                .places(request.getPlaces())
                .about(request.getAbout())
                .achievementRate(0.0f)
                .build();

        User savedUser = userRepository.save(user);
        return UserDto.fromEntity(savedUser);
    }

    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return UserDto.fromEntity(user);
    }

    public UserDto updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        UserDto updated = UserDto.fromEntity(user);

        if (request.getUserName() != null) {
            user.setUserName(request.getUserName());
        }
        if (request.getUserImage() != null) {
            user.setUserImage(request.getUserImage());
        }
        if (request.getDivisions() != null) {
            user.setDivisions(request.getDivisions());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getInterests() != null) {
            user.setInterests(request.getInterests());
        }
        if (request.getPlaces() != null) {
            user.setPlaces(request.getPlaces());
        }
        if (request.getAbout() != null) {
            user.setAbout(request.getAbout());
        }

        return convertToDto(userRepository.save(user));
    }

    public Boolean addNewPassword(UUID id, String rawPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordRepository.findById(id).isPresent()) {
            throw new RuntimeException("Password already exists");
        }

        Password newPassword = Password
                .builder()
                .userId(user.getId())
                .password(encodePassword(rawPassword))
                .build();

        passwordRepository.save(newPassword);

        return true;
    }
    public Boolean changeUserPassword(UUID id, ChangePasswordRequest request) {
        Password password = passwordRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(request.getPassword(), password.getPassword())) {
            throw new RuntimeException("Password is not correct");
        }

        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            throw new RuntimeException("New password cannot be null or empty");
        }

        password.setPassword(encodePassword(request.getNewPassword()));

        passwordRepository.save(password);

        return true;
    }

    private String encodePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        return new BCryptPasswordEncoder().encode(rawPassword);
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .userImage(user.getUserImage())
                .divisions(user.getDivisions())
                .phoneNumber(user.getPhoneNumber())
                .interests(user.getInterests())
                .places(user.getPlaces())
                .about(user.getAbout())
                .achievementRate(user.getAchievementRate())
                .build();
    }

}
