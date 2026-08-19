package pl.wojtczak.score_predictor.exception;

public class LeagueNotFoundException extends RuntimeException {

    public LeagueNotFoundException(Integer leagueId) {
        super("League with ID '" + leagueId + "' not found.");
    }

}