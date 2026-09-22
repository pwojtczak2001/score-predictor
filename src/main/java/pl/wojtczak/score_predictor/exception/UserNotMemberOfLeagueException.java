package pl.wojtczak.score_predictor.exception;

public class UserNotMemberOfLeagueException extends RuntimeException {

    public UserNotMemberOfLeagueException(Integer leagueId) {
        super("User is not a member of league with ID '" + leagueId + "'.");
    }
}