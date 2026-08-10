package pl.wojtczak.score_predictor.dto.response;

public class UserStatsResponse {

    private Integer predictionsCount;
    private Integer exactPredictionsCount;
    private Integer correctOutcomePredictionsCount;
    private Integer wrongPredictionsCount;
    private Double averagePointsPerPrediction;
    private Double predictionEfficiencyPercentage;

    private Double correctPredictionsPercentage;
    private Integer finishedPredictionsCount;
    private Integer totalPoints;

    public UserStatsResponse(Integer predictionsCount, Integer exactPredictionsCount, Integer correctOutcomePredictionsCount, Integer wrongPredictionsCount, Double averagePointsPerPrediction, Double predictionEfficiencyPercentage, Double correctPredictionsPercentage, Integer finishedPredictionsCount, Integer totalPoints) {
        this.predictionsCount = predictionsCount;
        this.exactPredictionsCount = exactPredictionsCount;
        this.correctOutcomePredictionsCount = correctOutcomePredictionsCount;
        this.wrongPredictionsCount = wrongPredictionsCount;
        this.averagePointsPerPrediction = averagePointsPerPrediction;
        this.predictionEfficiencyPercentage = predictionEfficiencyPercentage;
        this.correctPredictionsPercentage = correctPredictionsPercentage;
        this.finishedPredictionsCount = finishedPredictionsCount;
        this.totalPoints = totalPoints;
    }

    public UserStatsResponse() {
    }

    public Integer getPredictionsCount() {
        return predictionsCount;
    }

    public Integer getExactPredictionsCount() {
        return exactPredictionsCount;
    }

    public Integer getCorrectOutcomePredictionsCount() {
        return correctOutcomePredictionsCount;
    }

    public Integer getWrongPredictionsCount() {
        return wrongPredictionsCount;
    }

    public Double getAveragePointsPerPrediction() {
        return averagePointsPerPrediction;
    }

    public Double getPredictionEfficiencyPercentage() {
        return predictionEfficiencyPercentage;
    }

    public Double getCorrectPredictionsPercentage() {
        return correctPredictionsPercentage;
    }

    public Integer getFinishedPredictionsCount() {
        return finishedPredictionsCount;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

}
