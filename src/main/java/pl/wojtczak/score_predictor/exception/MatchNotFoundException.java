package pl.wojtczak.score_predictor.exception;

public class MatchNotFoundException extends RuntimeException {

    public MatchNotFoundException(String externalMatchId) {
        super("Match with external ID '" + externalMatchId + "' not found.");
    }

}