package pl.wojtczak.score_predictor.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Integer userId) {
        super("User with ID '" + userId + "' not found. ");
    }

    public UserNotFoundException(String username) {
        super("User with username '" + username + "' not found.");
    }

}