package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.RegisterUserRequest;
import ada.divercity.diverbook_server.dto.UpdateUserRequest;
import ada.divercity.diverbook_server.dto.UserDto;
import ada.divercity.diverbook_server.entity.User;
import ada.divercity.diverbook_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserDto createUser(RegisterUserRequest request) {
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
                .password(encodePassword(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        return UserDto.fromEntity(savedUser);
    }

    public UserDto getUserById(UUID id) {
        System.out.println("getUserById: " + id);
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return UserDto.fromEntity(user);
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
                .password(user.getPassword())
                .achievementRate(user.getAchievementRate())
                .build();
    }

    private String encodePassword(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }
}
