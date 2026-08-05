package pl.wojtczak.score_predictor.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.service.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    public UserDetails loadUserById(Integer userId) {
        return userService.getUserById(userId);
    }

}
