package pl.wojtczak.score_predictor.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.entity.LeagueMember;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.Prediction;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.repository.LeagueMemberRepository;
import pl.wojtczak.score_predictor.repository.PredictionRepository;

import java.util.*;

@Service
public class ScoringService {

    private static final int EXACT_SCORE_POINTS = 3;
    private static final int CORRECT_RESULT_POINTS = 1;
    private static final int INCORRECT_RESULT_POINTS = 0;

    private final PredictionRepository predictionRepository;

    private final LeagueMemberRepository leagueMemberRepository;


    public ScoringService(PredictionRepository predictionRepository, LeagueMemberRepository leagueMemberRepository) {
        this.predictionRepository = predictionRepository;
        this.leagueMemberRepository = leagueMemberRepository;
    }

    private int calculatePoints(Match match, Prediction prediction) {
        int actualHomeScore = match.getHomeScore();
        int actualAwayScore = match.getAwayScore();
        int predictedHomeScore = prediction.getPredictedHomeScore();
        int predictedAwayScore = prediction.getPredictedAwayScore();

        if (actualHomeScore == predictedHomeScore && actualAwayScore == predictedAwayScore) {
            return EXACT_SCORE_POINTS;
        } else if ((actualHomeScore > actualAwayScore && predictedHomeScore > predictedAwayScore)
                    ||
                   (actualHomeScore < actualAwayScore && predictedHomeScore < predictedAwayScore)
                    ||
                   (actualHomeScore == actualAwayScore && predictedHomeScore == predictedAwayScore)) {
            return CORRECT_RESULT_POINTS;
        } else {
            return INCORRECT_RESULT_POINTS;
        }
    }

    @Transactional
    public void calculateAndAwardPoints(Match match){

        List<Prediction> predictions = predictionRepository.findByMatch(match);
        Set<User> users = new HashSet<>();

        for (Prediction prediction : predictions) {
            users.add(prediction.getUser());
        }

        List<LeagueMember> allLeagueMembers = leagueMemberRepository.findByUserIn(users);
        Map<User, List<LeagueMember>> userLeagueMembersMap = new HashMap<>();

        for (LeagueMember leagueMember : allLeagueMembers) {
            if (!userLeagueMembersMap.containsKey(leagueMember.getUser())) {
                userLeagueMembersMap.put(leagueMember.getUser(), new ArrayList<>());
            }
            userLeagueMembersMap.get(leagueMember.getUser()).add(leagueMember);
        }


        for (Prediction prediction : predictions) {
            if(prediction.getPointsAwarded() != null) continue;
            int awardedPoints = calculatePoints(match, prediction);
            prediction.setPointsAwarded(awardedPoints);

            userLeagueMembersMap.get(prediction.getUser()).forEach(leagueMember -> {
                leagueMember.setCurrentPoints(leagueMember.getCurrentPoints() + awardedPoints);
                leagueMemberRepository.save(leagueMember);
            });

            predictionRepository.save(prediction);
        }
    }

}
