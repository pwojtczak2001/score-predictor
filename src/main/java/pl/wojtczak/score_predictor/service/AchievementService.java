package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.response.AchievementStatusResponse;
import pl.wojtczak.score_predictor.entity.Achievement;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.entity.UserAchievement;
import pl.wojtczak.score_predictor.exception.AchievementNotFoundException;
import pl.wojtczak.score_predictor.logging.GameEventLogger;
import pl.wojtczak.score_predictor.repository.AchievementRepository;
import pl.wojtczak.score_predictor.repository.PredictionRepository;
import pl.wojtczak.score_predictor.repository.UserAchievementRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final PredictionRepository predictionRepository;
    private final PlayerProgressionService playerProgressionService;

    private final GameEventLogger gameEventLogger;

    public AchievementService(AchievementRepository achievementRepository, UserAchievementRepository userAchievementRepository, PredictionRepository predictionRepository, PlayerProgressionService playerProgressionService, GameEventLogger gameEventLogger) {
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.predictionRepository = predictionRepository;
        this.playerProgressionService = playerProgressionService;
        this.gameEventLogger = gameEventLogger;
    }

    private void awardAchievement(User user, String code) {

        Achievement achievement = achievementRepository.findByCode(code)
                .orElseThrow(() -> new AchievementNotFoundException(code));

        if (userAchievementRepository.existsByUserAndAchievement(user, achievement)) {
            return;
        }

        UserAchievement userAchievement = new UserAchievement(user, achievement);

        userAchievementRepository.save(userAchievement);

        playerProgressionService.awardCoins(user, achievement.getCoinsReward());

        try {
            gameEventLogger.logXpAndCoinsAwarded(
                    user,
                    "ACHIEVEMENT_" + achievement.getCode(),
                    0,
                    achievement.getCoinsReward()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkExactScoreAchievements(User user) {

        long exactScoreCount = predictionRepository.countByUserAndPointsAwarded(user, 3);

        if (exactScoreCount >= 1) {
            awardAchievement(user, "EXACT_SCORE_FIRST");
        }

        if (exactScoreCount >= 10) {
            awardAchievement(user, "EXACT_SCORE_10");
        }

        if (exactScoreCount >= 25) {
            awardAchievement(user, "EXACT_SCORE_25");
        }

        if (exactScoreCount >= 50) {
            awardAchievement(user, "EXACT_SCORE_50");
        }

        if (exactScoreCount >= 100) {
            awardAchievement(user, "EXACT_SCORE_100");
        }

        if (exactScoreCount >= 200) {
            awardAchievement(user, "EXACT_SCORE_200");
        }
    }

    public List<AchievementStatusResponse> getAllAchievements(User user){

        List<UserAchievement> userAchievements = userAchievementRepository.findByUser(user);

        List<AchievementStatusResponse> result = new ArrayList<>();

        for (Achievement achievement : achievementRepository.findAll()) {
            UserAchievement userAchievement = null;
            for (UserAchievement ua : userAchievements) {
                if (ua.getAchievement().getCode().equals(achievement.getCode())) {
                    userAchievement = ua;
                    break;
                }
            }
            result.add(new AchievementStatusResponse(
                    achievement.getCode(),
                    achievement.getName(),
                    achievement.getCoinsReward(),
                    userAchievement != null,
                    userAchievement != null ? userAchievement.getUnlockedAt() : null
            ));
        }

        return result;
    }


}
