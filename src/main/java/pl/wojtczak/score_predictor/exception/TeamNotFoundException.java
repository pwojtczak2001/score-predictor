package pl.wojtczak.score_predictor.exception;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException(String name) {
        super("Team with name '" + name + "' not found. ");
    }

}