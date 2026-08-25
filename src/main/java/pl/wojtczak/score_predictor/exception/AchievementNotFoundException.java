package pl.wojtczak.score_predictor.exception;

public class AchievementNotFoundException extends RuntimeException {

    public AchievementNotFoundException(String code) {
        super("Achievement with code '" + code + "' not found. ");
    }

}