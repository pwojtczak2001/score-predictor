package pl.wojtczak.score_predictor.dto.request;

public class PredictionRequest {

    private String externalMatchId;
    private Integer predictedHomeScore;
    private Integer predictedAwayScore;

    public PredictionRequest() {
    }

    public PredictionRequest(String externalMatchId, Integer predictedHomeScore, Integer predictedAwayScore) {
        this.externalMatchId = externalMatchId;
        this.predictedHomeScore = predictedHomeScore;
        this.predictedAwayScore = predictedAwayScore;
    }


    public String getExternalMatchId() {
        return externalMatchId;
    }

    public void setExternalMatchId(String externalMatchId) {
        this.externalMatchId = externalMatchId;
    }

    public Integer getPredictedHomeScore() {
        return predictedHomeScore;
    }

    public void setPredictedHomeScore(Integer predictedHomeScore) {
        this.predictedHomeScore = predictedHomeScore;
    }

    public Integer getPredictedAwayScore() {
        return predictedAwayScore;
    }

    public void setPredictedAwayScore(Integer predictedAwayScore) {
        this.predictedAwayScore = predictedAwayScore;
    }

}
