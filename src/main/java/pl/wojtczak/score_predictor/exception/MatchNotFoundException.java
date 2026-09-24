package pl.wojtczak.score_predictor.exception;

public class MatchNotFoundException extends RuntimeException {

    public MatchNotFoundException(String externalMatchId) {
        super("Match with external ID '" + externalMatchId + "' not found.");
    }

    public MatchNotFoundException(Integer matchId) {
        super("Match with ID '" + matchId + "' not found.");
    }

}