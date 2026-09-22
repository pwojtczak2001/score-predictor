package pl.wojtczak.score_predictor.dto.request;

public class SpyTargetRequest {

    private Integer leagueId;
    private String stage;
    private Integer targetUserId;

    public SpyTargetRequest(Integer leagueId, String stage, Integer targetUserId) {
        this.leagueId = leagueId;
        this.stage = stage;
        this.targetUserId = targetUserId;
    }

    public SpyTargetRequest() {
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public String getStage() {
        return stage;
    }

    public Integer getTargetUserId() {
        return targetUserId;
    }

    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public void setTargetUserId(Integer targetUserId) {
        this.targetUserId = targetUserId;
    }
}