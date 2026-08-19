package pl.wojtczak.score_predictor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wojtczak.score_predictor.dto.auth.AuthResponse;
import pl.wojtczak.score_predictor.dto.auth.LoginRequest;
import pl.wojtczak.score_predictor.dto.auth.RegisterRequest;
import pl.wojtczak.score_predictor.enums.RegistrationStatus;
import pl.wojtczak.score_predictor.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {

        AuthResponse response = authService.register(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }

        return switch (response.getRegistrationStatus()) {
            case EMAIL_TAKEN, USERNAME_TAKEN ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(response);

            case INVALID_EMAIL, WEAK_PASSWORD ->
                    ResponseEntity.badRequest().body(response);

            default ->
                    throw new IllegalStateException(
                            "Unexpected registration status: " + response.getRegistrationStatus()
                    );
        };
    }

}
