package pl.wojtczak.score_predictor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wojtczak.score_predictor.entity.Achievement;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.entity.UserAchievement;
import pl.wojtczak.score_predictor.entity.UserAchievementId;

import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, UserAchievementId> {

    boolean existsByUserAndAchievement(User user, Achievement achievement);

    List<UserAchievement> findByUser(User user);

    long countByUser(User user);
}
