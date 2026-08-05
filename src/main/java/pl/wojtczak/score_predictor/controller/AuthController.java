package pl.wojtczak.score_predictor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wojtczak.score_predictor.dto.auth.AuthResponse;
import pl.wojtczak.score_predictor.dto.auth.LoginRequest;
import pl.wojtczak.score_predictor.dto.auth.RegisterRequest;
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
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

}
