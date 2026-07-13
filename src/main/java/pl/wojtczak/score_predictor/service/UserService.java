package pl.wojtczak.score_predictor.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.enums.RegistrationStatus;
import pl.wojtczak.score_predictor.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public RegistrationStatus registerUser(String username, String email, String password) {
        if (!email.contains("@")) {
            return RegistrationStatus.INVALID_EMAIL;
        }
        if (password.length() < 8) {
            return RegistrationStatus.WEAK_PASSWORD;
        }
        if(userRepository.existsByEmail(email)){
            return RegistrationStatus.EMAIL_TAKEN;
        }
        if(userRepository.existsByUsername(username)){
            return RegistrationStatus.USERNAME_TAKEN;
        }
        User user = new User(username, email, passwordEncoder.encode(password));
        userRepository.save(user);
        return RegistrationStatus.SUCCESS;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User with username '" + username + "' not found. " +
                                        "The state of the application is inconsistent with our business assumptions."));
    }


}
