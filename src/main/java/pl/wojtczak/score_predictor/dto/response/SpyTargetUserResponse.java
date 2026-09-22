package pl.wojtczak.score_predictor.dto.response;

public class SpyTargetUserResponse {

    private Integer userId;
    private String username;
    private long predictionsCount;
    private long totalMatches;

    public SpyTargetUserResponse(Integer userId, String username, long predictionsCount, long totalMatches) {
        this.userId = userId;
        this.username = username;
        this.predictionsCount = predictionsCount;
        this.totalMatches = totalMatches;
    }

    public SpyTargetUserResponse() {
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public long getPredictionsCount() {
        return predictionsCount;
    }

    public long getTotalMatches() {
        return totalMatches;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPredictionsCount(long predictionsCount) {
        this.predictionsCount = predictionsCount;
    }

    public void setTotalMatches(long totalMatches) {
        this.totalMatches = totalMatches;
    }

}
