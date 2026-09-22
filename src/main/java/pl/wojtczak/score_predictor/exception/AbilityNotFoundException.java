package pl.wojtczak.score_predictor.exception;

public class AbilityNotFoundException extends RuntimeException {

    public AbilityNotFoundException(String code) {
        super("Ability with code '" + code + "' not found. ");
    }

}