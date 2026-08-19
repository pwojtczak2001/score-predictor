package pl.wojtczak.score_predictor.dto.auth;

import pl.wojtczak.score_predictor.enums.RegistrationStatus;

public class AuthResponse {

    private String token;
    private boolean success;

    private Integer userId;

    private String username;

    private RegistrationStatus registrationStatus;

    public AuthResponse() {
    }

    public AuthResponse(String token, boolean success, Integer userId, String username, RegistrationStatus registrationStatus) {
        this.token = token;
        this.success = success;
        this.userId = userId;
        this.username = username;
        this.registrationStatus = registrationStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

}
