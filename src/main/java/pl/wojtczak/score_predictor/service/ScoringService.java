package pl.wojtczak.score_predictor.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.entity.LeagueMember;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.Prediction;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.logging.GameEventLogger;
import pl.wojtczak.score_predictor.repository.AbilityUsageRepository;
import pl.wojtczak.score_predictor.repository.LeagueMemberRepository;
import pl.wojtczak.score_predictor.repository.PredictionRepository;

import java.io.IOException;
import java.util.*;

@Service
public class ScoringService {

    private static final int EXACT_SCORE_POINTS = 3;
    private static final int CORRECT_RESULT_POINTS = 1;
    private static final int INCORRECT_RESULT_POINTS = 0;

    private final PredictionRepository predictionRepository;

    private final LeagueMemberRepository leagueMemberRepository;

    private final PlayerProgressionService playerProgressionService;
    private final AchievementService achievementService;

    private final AbilityUsageRepository abilityUsageRepository;

    private final GameEventLogger gameEventLogger;

    public ScoringService(PredictionRepository predictionRepository, LeagueMemberRepository leagueMemberRepository, PlayerProgressionService playerProgressionService, AchievementService achievementService, AbilityUsageRepository abilityUsageRepository, GameEventLogger gameEventLogger) {
        this.predictionRepository = predictionRepository;
        this.leagueMemberRepository = leagueMemberRepository;
        this.playerProgressionService = playerProgressionService;
        this.achievementService = achievementService;
        this.abilityUsageRepository = abilityUsageRepository;
        this.gameEventLogger = gameEventLogger;
    }

    private void awardHotStreakEntryReward(User user) {

        playerProgressionService.awardXp(user, 5);
        playerProgressionService.awardCoins(user, 5);

        try {
            gameEventLogger.logXpAndCoinsAwarded(
                    user,
                    "HOT_STREAK_ENTRY",
                    5,
                    5
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int processHotStreak(User user, int normalAwardedPoints) {

        boolean hasHotStreak = abilityUsageRepository
                .existsByUserAndAbility_Code(
                        user,
                        "HOT_STREAK"
                );

        if (!hasHotStreak) {
            return normalAwardedPoints;
        }

        if (normalAwardedPoints == 0) {

            user.setExactScoreStreak(0);
            user.setCorrectResultStreak(0);
            user.setHotStreakActive(false);

            return normalAwardedPoints;
        }

        boolean wasHotStreakActive = user.getHotStreakActive();

        if (normalAwardedPoints == 3) {

            user.setExactScoreStreak(
                    user.getExactScoreStreak() + 1
            );

            user.setCorrectResultStreak(
                    user.getCorrectResultStreak() + 1
            );

        } else if (normalAwardedPoints == 1) {

            user.setExactScoreStreak(0);

            user.setCorrectResultStreak(
                    user.getCorrectResultStreak() + 1
            );
        }

        boolean hotStreakReached =
                user.getExactScoreStreak() >= 2
                        || user.getCorrectResultStreak() >= 3;

        if (!wasHotStreakActive && hotStreakReached) {

            try {
                gameEventLogger.logHotStreakEntered(user);
            } catch (IOException e) {
                e.printStackTrace();
            }

            user.setHotStreakActive(true);

            awardHotStreakEntryReward(user);

            return normalAwardedPoints;
        }

        if (wasHotStreakActive) {
            return normalAwardedPoints * 2;
        }

        return normalAwardedPoints;
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
            int normalAwardedPoints = calculatePoints(match, prediction);

            int awardedPoints = processHotStreak(
                    prediction.getUser(),
                    normalAwardedPoints
            );

            playerProgressionService.processPredictionResult(
                    prediction.getUser(),
                    awardedPoints
            );

            prediction.setPointsAwarded(awardedPoints);

            try {
                gameEventLogger.logPointsAwarded(prediction.getUser(), match, awardedPoints);
            } catch (IOException e) {
                e.printStackTrace();
            }

            userLeagueMembersMap.get(prediction.getUser()).forEach(leagueMember -> {
                leagueMember.setCurrentPoints(leagueMember.getCurrentPoints() + awardedPoints);
                leagueMemberRepository.save(leagueMember);
            });

            predictionRepository.save(prediction);
            achievementService.checkExactScoreAchievements(prediction.getUser());
        }
    }

}
