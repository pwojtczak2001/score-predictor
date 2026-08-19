package pl.wojtczak.score_predictor.exception;

public class MatchAlreadyExistsException extends RuntimeException{

    public MatchAlreadyExistsException(String externalMatchId) {
        super("Match with external ID '" + externalMatchId + "' already exists.");
    }
}
