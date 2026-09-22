package pl.wojtczak.score_predictor.dto.response;

import java.time.LocalDateTime;

public class SpyPredictionResponse {

    private String externalMatchId;
    private String homeTeam;
    private String awayTeam;
    private String stage;
    private String homeTeamLogoUrl;
    private String awayTeamLogoUrl;
    private LocalDateTime matchDate;
    private Integer predictedHomeScore;
    private Integer predictedAwayScore;

    public SpyPredictionResponse(String externalMatchId, String homeTeam, String awayTeam, String stage, String homeTeamLogoUrl, String awayTeamLogoUrl, LocalDateTime matchDate, Integer predictedHomeScore, Integer predictedAwayScore) {
        this.externalMatchId = externalMatchId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.stage = stage;
        this.homeTeamLogoUrl = homeTeamLogoUrl;
        this.awayTeamLogoUrl = awayTeamLogoUrl;
        this.matchDate = matchDate;
        this.predictedHomeScore = predictedHomeScore;
        this.predictedAwayScore = predictedAwayScore;
    }

    public SpyPredictionResponse() {
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

    public String getHomeTeamLogoUrl() {
        return homeTeamLogoUrl;
    }

    public String getAwayTeamLogoUrl() {
        return awayTeamLogoUrl;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public Integer getPredictedHomeScore() {
        return predictedHomeScore;
    }

    public Integer getPredictedAwayScore() {
        return predictedAwayScore;
    }

    public void setExternalMatchId(String externalMatchId) {
        this.externalMatchId = externalMatchId;
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

    public void setHomeTeamLogoUrl(String homeTeamLogoUrl) {
        this.homeTeamLogoUrl = homeTeamLogoUrl;
    }

    public void setAwayTeamLogoUrl(String awayTeamLogoUrl) {
        this.awayTeamLogoUrl = awayTeamLogoUrl;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public void setPredictedHomeScore(Integer predictedHomeScore) {
        this.predictedHomeScore = predictedHomeScore;
    }

    public void setPredictedAwayScore(Integer predictedAwayScore) {
        this.predictedAwayScore = predictedAwayScore;
    }

}