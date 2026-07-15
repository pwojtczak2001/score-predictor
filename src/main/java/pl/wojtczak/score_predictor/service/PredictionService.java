package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.Prediction;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.enums.PredictionStatus;
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

    public PredictionStatus addPrediction(Match match, User user, int homeScore, int awayScore) {
        LocalDateTime matchDate = match.getMatchDate();

        if (matchDate.isBefore(java.time.LocalDateTime.now())) {
            return PredictionStatus.MATCH_ALREADY_STARTED;
        }

        if (predictionRepository.existsByMatchAndUser(match, user)) {
            return PredictionStatus.PREDICTION_ALREADY_EXISTS;
        }

        Prediction prediction = new Prediction(match, user, homeScore, awayScore);
        predictionRepository.save(prediction);

        return PredictionStatus.SUCCESS;
    }

    private Prediction getPrediction(Match match, User user) {
        return predictionRepository.findByMatchAndUser(match, user)
                .orElseThrow(() -> new IllegalArgumentException("Prediction not found for the given match and user."));
    }

    public PredictionStatus updatePrediction(Match match, User user, int homeScore, int awayScore) {

        Optional<Prediction> optionalPrediction = predictionRepository.findByMatchAndUser(match, user);

        LocalDateTime matchDate = match.getMatchDate();

        if (optionalPrediction.isEmpty()) {
            return PredictionStatus.PREDICTION_NOT_FOUND;
        }

        if (matchDate.isBefore(java.time.LocalDateTime.now())) {
            return PredictionStatus.MATCH_ALREADY_STARTED;
        }

        Prediction prediction = optionalPrediction.get();

        prediction.setPredictedHomeScore(homeScore);
        prediction.setPredictedAwayScore(awayScore);
        predictionRepository.save(prediction);

        return PredictionStatus.SUCCESS;
    }

    public List<Prediction> getPredictionsByUser(User user) {
        return predictionRepository.findByUser(user);
    }

}
