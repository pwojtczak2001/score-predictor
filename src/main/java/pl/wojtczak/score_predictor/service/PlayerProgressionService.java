package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.logging.GameEventLogger;

import java.io.IOException;

@Service
public class PlayerProgressionService {

    private static final int XP_PER_LEVEL = 9;
    private final GameEventLogger gameEventLogger;

    public PlayerProgressionService(GameEventLogger gameEventLogger) {
        this.gameEventLogger = gameEventLogger;
    }

    public void awardXp(User user, int amount) {
        int oldLevel = user.getLevel();

        user.setXp(user.getXp() + amount);

        int newLevel = user.getXp() / XP_PER_LEVEL + 1;
        user.setLevel(newLevel);

        if (newLevel > oldLevel) {
            awardCoins(user, (newLevel - oldLevel) * 10);
            try {
                gameEventLogger.logXpAndCoinsAwarded(
                        user,
                        "LEVEL_UP_BONUS",
                        0,
                        (newLevel - oldLevel) * 10
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void awardCoins(User user, int amount) {
        user.setCoins(user.getCoins() + amount);
    }

    public void processPredictionResult(User user, int pointsAwarded) {

        awardXp(user, 1);

        if (pointsAwarded == 0) {
            try {
                gameEventLogger.logXpAndCoinsAwarded(
                        user,
                        "INCORRECT_PREDICTION",
                        1,
                        0
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (pointsAwarded == 1 || pointsAwarded == 2) {
            awardXp(user, 1);
            awardCoins(user, 1);
            try {
                gameEventLogger.logXpAndCoinsAwarded(
                        user,
                        "CORRECT_PREDICTION",
                        2,
                        1
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (pointsAwarded == 3 || pointsAwarded == 6) {
            awardXp(user, 3);
            awardCoins(user, 3);
            try {
                gameEventLogger.logXpAndCoinsAwarded(
                        user,
                        "EXACT_PREDICTION",
                        4,
                        3
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
