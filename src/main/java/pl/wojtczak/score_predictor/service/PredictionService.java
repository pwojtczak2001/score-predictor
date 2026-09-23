package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.response.UserStatsResponse;
import pl.wojtczak.score_predictor.entity.League;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.Prediction;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.enums.PredictionOperationStatus;
import pl.wojtczak.score_predictor.exception.UserNotMemberOfLeagueException;
import pl.wojtczak.score_predictor.repository.AbilityUsageRepository;
import pl.wojtczak.score_predictor.repository.LeagueMemberRepository;
import pl.wojtczak.score_predictor.repository.PredictionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PredictionService {

    private final PredictionRepository predictionRepository;

    private final AbilityUsageRepository abilityUsageRepository;

    private final LeagueMemberRepository leagueMemberRepository;

    public PredictionService(PredictionRepository predictionRepository, AbilityUsageRepository abilityUsageRepository, LeagueMemberRepository leagueMemberRepository) {
        this.predictionRepository = predictionRepository;
        this.abilityUsageRepository = abilityUsageRepository;
        this.leagueMemberRepository = leagueMemberRepository;
    }


    public PredictionOperationStatus addPrediction(Match match, User user, League league, int homeScore, int awayScore) {

        if (!leagueMemberRepository.existsByLeagueAndUser(league, user)) {
            throw new UserNotMemberOfLeagueException(league.getLeagueId());
        }

        LocalDateTime matchDate = match.getMatchDate();

        if (!matchDate.isAfter(LocalDateTime.now())) {
            return PredictionOperationStatus.MATCH_ALREADY_STARTED;
        }

        if (predictionRepository.existsByMatchAndUserAndLeague(match, user, league)) {
            return PredictionOperationStatus.PREDICTION_ALREADY_EXISTS;
        }

        if (abilityUsageRepository
                .existsByTargetUserAndTargetMatchAndLeagueAndAbility_Code(
                        user,
                        match,
                        league,
                        "BOMB"
                )) {
            return PredictionOperationStatus.PREDICTION_BOMBED;
        }

        Prediction prediction = new Prediction(match, user, league, homeScore, awayScore);
        predictionRepository.save(prediction);

        return PredictionOperationStatus.SUCCESS;
    }

    public PredictionOperationStatus updatePrediction(Match match, User user, League league, int homeScore, int awayScore) {

        if (!leagueMemberRepository.existsByLeagueAndUser(league, user)) {
            throw new UserNotMemberOfLeagueException(league.getLeagueId());
        }

        Optional<Prediction> optionalPrediction = predictionRepository.findByMatchAndUserAndLeague(match, user, league);

        LocalDateTime matchDate = match.getMatchDate();

        if (optionalPrediction.isEmpty()) {
            return PredictionOperationStatus.PREDICTION_NOT_FOUND;
        }

        if (!matchDate.isAfter(LocalDateTime.now())) {
            return PredictionOperationStatus.MATCH_ALREADY_STARTED;
        }

        if (abilityUsageRepository
                .existsByTargetUserAndTargetMatchAndLeagueAndAbility_Code(
                        user, match, league, "LOCK")) {
            return PredictionOperationStatus.PREDICTION_LOCKED;
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

    public UserStatsResponse getUserStats (User user, League league){

            List<Prediction> predictions = predictionRepository.findByUserAndLeague(user, league);
            int totalPredictions = predictions.size();
            int exactPredictions = 0;
            int correctOutcomePredictions = 0;
            int wrongPredictions = 0;
            int finishedPredictions = 0;
            int totalPoints = 0;
            double averagePointsPerPrediction;
            double correctPredictionsPercentage;
            for (Prediction prediction : predictions) {
                if (prediction.getPointsAwarded() == null) {
                    continue;
                }
                finishedPredictions++;
                totalPoints += prediction.getPointsAwarded();
                switch (prediction.getPointsAwarded()) {
                    case 3, 6 -> exactPredictions++;
                    case 1, 2 -> correctOutcomePredictions++;
                    case 0 -> wrongPredictions++;
                }
            }
            averagePointsPerPrediction = finishedPredictions > 0 ? (double) totalPoints / finishedPredictions : 0;
            correctPredictionsPercentage = finishedPredictions > 0 ? (double) (exactPredictions + correctOutcomePredictions) / finishedPredictions * 100 : 0;

            averagePointsPerPrediction = Math.round(averagePointsPerPrediction * 100.0) / 100.0;
            correctPredictionsPercentage = Math.round(correctPredictionsPercentage * 100.0) / 100.0;

            return new UserStatsResponse(totalPredictions, exactPredictions, correctOutcomePredictions, wrongPredictions, averagePointsPerPrediction, correctPredictionsPercentage, finishedPredictions, totalPoints);
    }

}
