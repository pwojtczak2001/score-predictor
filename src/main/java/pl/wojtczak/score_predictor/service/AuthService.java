package pl.wojtczak.score_predictor.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.auth.AuthResponse;
import pl.wojtczak.score_predictor.dto.auth.LoginRequest;
import pl.wojtczak.score_predictor.dto.auth.RegisterRequest;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.enums.RegistrationStatus;
import pl.wojtczak.score_predictor.security.JwtService;

@Service
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;


    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));
        User user = userService.getUserByUsername(request.getUsername());
        String token = jwtService.generateToken(user.getUserId());
        return new AuthResponse(token, true, user.getUserId(), user.getUsername(), null);
    }

    public AuthResponse register(RegisterRequest request) {
        RegistrationStatus status = userService.registerUser(request);

        if (status != RegistrationStatus.SUCCESS) {
            return new AuthResponse(
                    null,
                    false,
                    null,
                    null,
                    status
            );

        }

        User user = userService.getUserByUsername(request.getUsername());
        String token = jwtService.generateToken(user.getUserId());
        return new AuthResponse(
                token,
                true,
                user.getUserId(),
                user.getUsername(),
                RegistrationStatus.SUCCESS
        );
    }
}