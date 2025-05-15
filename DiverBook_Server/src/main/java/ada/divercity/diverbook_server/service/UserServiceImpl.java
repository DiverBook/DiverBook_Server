package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.*;
import ada.divercity.diverbook_server.entity.Password;
import ada.divercity.diverbook_server.entity.User;
import ada.divercity.diverbook_server.exception.CustomException;
import ada.divercity.diverbook_server.exception.ErrorCode;
import ada.divercity.diverbook_server.repository.PasswordRepository;
import ada.divercity.diverbook_server.repository.UserRepository;
import ada.divercity.diverbook_server.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;
    private final TokenBlackListService tokenBlackListService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CollectionService collectionService;

    public UserDto createUser(RegisterUserRequest request) {
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        User user = User.builder()
                .userName(request.getUserName())
                .divisions(request.getDivisions())
                .phoneNumber(request.getPhoneNumber())
                .interests(request.getInterests())
                .places(request.getPlaces())
                .about(request.getAbout())
                .build();

        User savedUser = userRepository.save(user);
        return UserDto.fromEntity(savedUser);
    }

    public UserDto activateUser(RegisterUserRequest request) {
        User user = userRepository.findByUserName(request.getUserName()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.setDivisions(request.getDivisions());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setInterests(request.getInterests());
        user.setPlaces(request.getPlaces());
        user.setAbout(request.getAbout());
        user.setIsActivated(true);

        return UserDto.fromEntity(userRepository.save(user));
    }

    @Transactional
    public UserDto deactivateUser(UUID id, DeactivateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (request.getRefreshToken() != null && !request.getRefreshToken().isEmpty()) {
            String tokenUserId = jwtTokenProvider.validateRefreshTokenAndGetUserId(request.getRefreshToken());
            if (!tokenUserId.equals(user.getId().toString())) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
            tokenBlackListService.addTokenToBlackList(request.getRefreshToken());
        }

        user.setDivisions(null);
        user.setPhoneNumber(null);
        user.setInterests(null);
        user.setPlaces(null);
        user.setAbout(null);
        user.setIsActivated(false);

        passwordRepository.deleteByUserId(id);

        collectionService.deleteAllCollections(user.getId());

        return UserDto.fromEntity(userRepository.save(user));
    }

    public Boolean getUserActivationStatusByName(String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user.getIsActivated();
    }

    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserDto.fromEntity(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "userName"));
        return users.stream()
                .map(UserDto::fromEntity)
                .toList();
    }

    public String getProfileImageUrlByName(String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user.getProfileImageUrl();
    }

    @Transactional(readOnly = true)
    public Float getAchievementRateById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return (long) user.getCollections().size() / (float) (userRepository.count() - 1) * 100;
    }

    public UserDto updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        UserDto updated = UserDto.fromEntity(user);

        if (request.getUserName() != null) {
            user.setUserName(request.getUserName());
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

        return UserDto.fromEntity(userRepository.save(user));
    }

    public Boolean addNewPassword(UUID id, String rawPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (passwordRepository.findById(id).isPresent()) {
            throw new CustomException(ErrorCode.PASSWORD_ALREADY_EXISTS);
        }

        Password newPassword = Password
                .builder()
                .userId(user.getId())
                .password(encodePassword(rawPassword))
                .build();

        passwordRepository.save(newPassword);

        return true;
    }
    public String changeUserPassword(UUID id, ChangePasswordRequest request) {
        Password password = passwordRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.PASSWORD_NOT_FOUND));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(request.getPassword(), password.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            throw new CustomException(ErrorCode.MISSING_REQUIRED_FIELD);
        }

        if (request.getRefreshToken() != null && !request.getRefreshToken().isEmpty()) {
            String tokenUserId = jwtTokenProvider.validateRefreshTokenAndGetUserId(request.getRefreshToken());
            if (!tokenUserId.equals(id.toString())) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
            tokenBlackListService.addTokenToBlackList(request.getRefreshToken());
        }

        password.setPassword(encodePassword(request.getNewPassword()));

        String refreshToken = jwtTokenProvider.generateRefreshToken(password.getUserId().toString());

        passwordRepository.save(password);

        return refreshToken;
    }

    private String encodePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new CustomException(ErrorCode.MISSING_REQUIRED_FIELD);
        }

        return new BCryptPasswordEncoder().encode(rawPassword);
    }

}
