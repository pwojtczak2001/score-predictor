package pl.wojtczak.score_predictor.logging;

import jakarta.persistence.Column;
import org.springframework.stereotype.Component;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.User;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class GameEventLogger {

    public void logPointsAwarded(User user, Match match, int points) throws IOException {
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

        try (FileWriter writer = new FileWriter("points_awarded_log.txt", true)) {
            writer.write(message + System.lineSeparator());
        }
    }

    public void logHotStreakEntered(User user) throws IOException {
        String message = String.format(
                "%s | HOT_STREAK_ENTERED | user=%s",
                LocalDateTime.now(),
                user.getUsername()
        );

        try (FileWriter writer = new FileWriter("points_awarded_log.txt", true)) {
            writer.write(message + System.lineSeparator());
        }
    }

    public void logXpAndCoinsAwarded(User user, String source, int xp, int coins) throws IOException {

        String message = String.format(
                "%s | user=%s | source=%s | xp=%d | coins=%d",
                LocalDateTime.now(),
                user.getUsername(),
                source,
                xp,
                coins
        );

        try (FileWriter writer = new FileWriter("coins_xp_log.txt", true)) {
            writer.write(message + System.lineSeparator());
        }
    }

}
