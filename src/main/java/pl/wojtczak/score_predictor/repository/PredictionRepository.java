package pl.wojtczak.score_predictor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.wojtczak.score_predictor.entity.*;

import java.util.List;
import java.util.Optional;

public interface PredictionRepository extends JpaRepository<Prediction, Integer> {

    List<Prediction> findByMatch(Match match);

    List<Prediction> findByUser(User user);

    Optional<Prediction> findByMatchAndUserAndLeague(
            Match match,
            User user,
            League league
    );

    boolean existsByMatchAndUserAndLeague(
            Match match,
            User user,
            League league
    );

    void deleteByMatch(Match match);

    long countByUserAndLeague(User user, League league);

    List<Prediction> findByUserAndLeagueAndMatch_Stage(User user, League league, String stage);

    long countByUserAndPointsAwarded(User user, Integer pointsAwarded);

    List<Prediction> findByUserAndLeague(User user, League league);

    @Query("""
        SELECT p.user.userId, p.user.username, COUNT(p)
        FROM Prediction p
        WHERE p.league = :league
          AND p.match.stage = :stage
          AND p.user <> :currentUser
        GROUP BY p.user.userId, p.user.username
        ORDER BY COUNT(p) DESC
        """)
    List<Object[]> findSpyTargetUsers(
            League league,
            String stage,
            User currentUser
    );

    boolean existsByUserAndLeagueAndMatch_Stage(User user, League league, String stage);
}