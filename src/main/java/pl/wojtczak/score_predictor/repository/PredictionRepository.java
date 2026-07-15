package pl.wojtczak.score_predictor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wojtczak.score_predictor.entity.*;

import java.util.List;
import java.util.Optional;

public interface PredictionRepository extends JpaRepository<Prediction, Integer> {

    List<Prediction> findByMatch(Match match);
    List<Prediction> findByUser(User user);
    Optional<Prediction> findByMatchAndUser(Match match, User user);
    boolean existsByMatchAndUser(Match match, User user);
}
