package pl.wojtczak.score_predictor.logging;

import org.springframework.stereotype.Component;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.User;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class GameEventLogger {

    private void writeToFile(String fileName, String message) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(message + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logPointsAwarded(User user, Match match, int points) {
        String homeTeam = match.getHomeTeam().getDisplayName();
        String awayTeam = match.getAwayTeam().getDisplayName();

        String message = String.format(
                "%s | user=%s | match=%s vs %s | points=%d",
                LocalDateTime.now(),
                user.getUsername(),
                homeTeam,
                awayTeam,
                points
        );

        writeToFile(
                "points_awarded_log.txt",
                message
        );
    }

    public void logHotStreakEntered(User user) {
        String message = String.format(
                "%s | HOT_STREAK_ENTERED | user=%s",
                LocalDateTime.now(),
                user.getUsername()
        );

        writeToFile(
                "points_awarded_log.txt",
                message
        );
    }

    public void logXpAndCoinsAwarded(User user, String source, int xp, int coins) {

        String message = String.format(
                "%s | user=%s | source=%s | xp=%d | coins=%d",
                LocalDateTime.now(),
                user.getUsername(),
                source,
                xp,
                coins
        );

        writeToFile(
                "coins_xp_log.txt",
                message
        );
    }

    public void logAbilityUsed(
            User user,
            String abilityCode,
            User targetUser,
            Match targetMatch
    ) {
        StringBuilder message = new StringBuilder();

        message.append(String.format(
                "%s | user=%s | ability=%s",
                LocalDateTime.now(),
                user.getUsername(),
                abilityCode
        ));

        if (targetUser != null) {
            message.append(String.format(
                    " | targetUser=%s",
                    targetUser.getUsername()
            ));
        }

        if (targetMatch != null) {
            message.append(String.format(
                    " | match=%s vs %s",
                    targetMatch.getHomeTeam().getDisplayName(),
                    targetMatch.getAwayTeam().getDisplayName()
            ));
        }

        writeToFile(
                "used_abilities_log.txt",
                message.toString()
        );
    }

}
