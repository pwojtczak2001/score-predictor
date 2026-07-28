package pl.wojtczak.score_predictor.dto.league;

public class LeagueRankingDto {

    private final int position;
    private final String username;
    private final int points;

    public LeagueRankingDto(int position, String username, int points) {
        this.position = position;
        this.username = username;
        this.points = points;
    }

    public int getPosition() {
        return position;
    }

    public String getUsername() {
        return username;
    }

    public int getPoints() {
        return points;
    }

}
