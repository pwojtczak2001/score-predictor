package pl.wojtczak.score_predictor.dto.response;

import java.time.LocalDateTime;

public class UpcomingMatchResponse {

    private String externalMatchId;

    private String homeTeam;

    private String awayTeam;

    private String stage;

    private String homeLogoUrl;

    private String awayLogoUrl;

    private LocalDateTime matchDate;

    private String status;

    private Integer predictionId;

    private Integer predictedHomeScore;

    private Integer predictedAwayScore;

    public UpcomingMatchResponse(String externalMatchId, String homeTeam, String awayTeam, String stage, String homeLogoUrl, String awayLogoUrl, LocalDateTime matchDate, String status, Integer predictedHomeScore, Integer predictedAwayScore) {
        this.externalMatchId = externalMatchId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.stage = stage;
        this.homeLogoUrl = homeLogoUrl;
        this.awayLogoUrl = awayLogoUrl;
        this.matchDate = matchDate;
        this.status = status;
        this.predictedHomeScore = predictedHomeScore;
        this.predictedAwayScore = predictedAwayScore;
    }

    public UpcomingMatchResponse() {
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

    public Integer getPredictedHomeScore() {
        return predictedHomeScore;
    }

    public Integer getPredictedAwayScore() {
        return predictedAwayScore;
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


    public void setPredictedHomeScore(Integer predictedHomeScore) {
        this.predictedHomeScore = predictedHomeScore;
    }

    public void setPredictedAwayScore(Integer predictedAwayScore) {
        this.predictedAwayScore = predictedAwayScore;
    }

}
