package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.response.UserStatsResponse;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.Prediction;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.enums.PredictionOperationStatus;
import pl.wojtczak.score_predictor.repository.PredictionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PredictionService {

    private final PredictionRepository predictionRepository;

    public PredictionService(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }

    public PredictionOperationStatus addPrediction(Match match, User user, int homeScore, int awayScore) {
        LocalDateTime matchDate = match.getMatchDate();

        if (!matchDate.isAfter(LocalDateTime.now())) {
            return PredictionOperationStatus.MATCH_ALREADY_STARTED;
        }

        if (predictionRepository.existsByMatchAndUser(match, user)) {
            return PredictionOperationStatus.PREDICTION_ALREADY_EXISTS;
        }

        Prediction prediction = new Prediction(match, user, homeScore, awayScore);
        predictionRepository.save(prediction);

        return PredictionOperationStatus.SUCCESS;
    }

    public PredictionOperationStatus updatePrediction(Match match, User user, int homeScore, int awayScore) {

        Optional<Prediction> optionalPrediction = predictionRepository.findByMatchAndUser(match, user);

        LocalDateTime matchDate = match.getMatchDate();

        if (optionalPrediction.isEmpty()) {
            return PredictionOperationStatus.PREDICTION_NOT_FOUND;
        }

        if (!matchDate.isAfter(LocalDateTime.now())) {
            return PredictionOperationStatus.MATCH_ALREADY_STARTED;
        }

        Prediction prediction = optionalPrediction.get();

        prediction.setPredictedHomeScore(homeScore);
        prediction.setPredictedAwayScore(awayScore);
        predictionRepository.save(prediction);

        return PredictionOperationStatus.SUCCESS;
    }

    public List<Prediction> getPredictionsByUser(User user) {
        return predictionRepository.findByUser(user);
    }

    public UserStatsResponse getUserStats (User user){

            List<Prediction> predictions = getPredictionsByUser(user);
            int totalPredictions = predictions.size();
            int exactPredictions = 0;
            int correctOutcomePredictions = 0;
            int wrongPredictions = 0;
            int finishedPredictions = 0;
            int totalPoints = 0;
            double averagePointsPerPrediction = 0;
            double predictionEfficiencyPercentage = 0;
            double correctPredictionsPercentage = 0;
            for (Prediction prediction : predictions) {
                if (prediction.getPointsAwarded() == null) {
                    continue;
                }
                finishedPredictions++;
                totalPoints += prediction.getPointsAwarded();
                switch (prediction.getPointsAwarded()) {
                    case 3 -> exactPredictions++;
                    case 1 -> correctOutcomePredictions++;
                    case 0 -> wrongPredictions++;
                }
            }
            averagePointsPerPrediction = finishedPredictions > 0 ? (double) totalPoints / finishedPredictions : 0;
            predictionEfficiencyPercentage = finishedPredictions > 0 ? (double) (totalPoints) / (finishedPredictions * 3) * 100 : 0;
            correctPredictionsPercentage = finishedPredictions > 0 ? (double) (exactPredictions + correctOutcomePredictions) / finishedPredictions * 100 : 0;

            averagePointsPerPrediction = Math.round(averagePointsPerPrediction * 100.0) / 100.0;
            predictionEfficiencyPercentage = Math.round(predictionEfficiencyPercentage * 100.0) / 100.0;
            correctPredictionsPercentage = Math.round(correctPredictionsPercentage * 100.0) / 100.0;

            return new UserStatsResponse(totalPredictions, exactPredictions, correctOutcomePredictions, wrongPredictions, averagePointsPerPrediction, predictionEfficiencyPercentage, correctPredictionsPercentage, finishedPredictions, totalPoints);
    }

}
