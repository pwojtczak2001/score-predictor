package pl.wojtczak.score_predictor.dto.response;

import java.time.LocalDateTime;

public class UpcomingThreeMatchesResponse {

    private String externalMatchId;

    private String homeTeam;

    private String awayTeam;

    private String stage;

    private String homeLogoUrl;

    private String awayLogoUrl;

    private LocalDateTime matchDate;

    private String status;


    public UpcomingThreeMatchesResponse(String externalMatchId, String homeTeam, String awayTeam, String stage, String homeLogoUrl, String awayLogoUrl, LocalDateTime matchDate, String status) {
        this.externalMatchId = externalMatchId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.stage = stage;
        this.homeLogoUrl = homeLogoUrl;
        this.awayLogoUrl = awayLogoUrl;
        this.matchDate = matchDate;
        this.status = status;
    }

    public UpcomingThreeMatchesResponse() {
    }

    public String getExternalMatchId() {
        return externalMatchId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getStage() {
        return stage;
    }

    public String getHomeLogoUrl() {
        return homeLogoUrl;
    }

    public String getAwayLogoUrl() {
        return awayLogoUrl;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public String getStatus() {
        return status;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public void setHomeLogoUrl(String homeLogoUrl) {
        this.homeLogoUrl = homeLogoUrl;
    }

    public void setAwayLogoUrl(String awayLogoUrl) {
        this.awayLogoUrl = awayLogoUrl;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
