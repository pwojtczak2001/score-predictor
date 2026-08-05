package pl.wojtczak.score_predictor.dto.auth;

public class AuthResponse {

    private String token;
    private boolean success;
    private String message;

    private Integer userId;

    private String username;

    public AuthResponse() {
    }

    public AuthResponse(String token, boolean success, String message, Integer userId, String username) {
        this.token = token;
        this.success = success;
        this.message = message;
        this.userId = userId;
        this.username = username;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

}
