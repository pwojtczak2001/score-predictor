package pl.wojtczak.score_predictor.exception;

public class PredictionNotFoundException extends RuntimeException{

    public PredictionNotFoundException(Integer matchId, String username, Integer leagueId) {
        super("User " + username + " doesn't have a prediction for match " + matchId + " in league with ID " + leagueId);
    }

}
