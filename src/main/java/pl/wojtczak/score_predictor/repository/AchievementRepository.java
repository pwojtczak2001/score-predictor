package pl.wojtczak.score_predictor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wojtczak.score_predictor.entity.Achievement;

import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Integer> {

    Optional<Achievement> findByCode(String code);
}