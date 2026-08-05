package pl.wojtczak.score_predictor.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.auth.RegisterRequest;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.enums.RegistrationStatus;
import pl.wojtczak.score_predictor.repository.UserRepository;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public RegistrationStatus registerUser(RegisterRequest request) {
        if (!request.getEmail().contains("@")) {
            return RegistrationStatus.INVALID_EMAIL;
        }
        if (request.getPassword().length() < 8) {
            return RegistrationStatus.WEAK_PASSWORD;
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return RegistrationStatus.EMAIL_TAKEN;
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return RegistrationStatus.USERNAME_TAKEN;
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);
        return RegistrationStatus.SUCCESS;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User with username '" + username + "' not found. " +
                                        "The state of the application is inconsistent with our business assumptions."));
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User with ID '" + userId + "' not found. " +
                                        "The state of the application is inconsistent with our business assumptions."));
    }


}
