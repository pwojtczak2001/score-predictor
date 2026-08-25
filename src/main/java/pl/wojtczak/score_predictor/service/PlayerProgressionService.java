package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.entity.User;

@Service
public class PlayerProgressionService {

    private static final int XP_PER_LEVEL = 9;

    public void awardXp(User user, int amount) {
        int oldLevel = user.getLevel();

        user.setXp(user.getXp() + amount);

        int newLevel = user.getXp() / XP_PER_LEVEL + 1;
        user.setLevel(newLevel);

        if (newLevel > oldLevel) {
            awardCoins(user, (newLevel - oldLevel) * 10);
        }
    }

    public void awardCoins(User user, int amount) {
        user.setCoins(user.getCoins() + amount);
    }

    public void processPredictionResult(User user, int pointsAwarded) {

        awardXp(user, 1);

        if (pointsAwarded == 1) {
            awardXp(user, 1);
            awardCoins(user, 1);
        }

        if (pointsAwarded == 3) {
            awardXp(user, 3);
            awardCoins(user, 3);
        }
    }

}
