package pl.wojtczak.score_predictor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wojtczak.score_predictor.entity.AbilityUsage;
import pl.wojtczak.score_predictor.entity.League;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.enums.AbilityType;

import java.util.List;

public interface AbilityUsageRepository extends JpaRepository<AbilityUsage, Integer> {

    boolean existsByUserAndLeagueAndStageAndAbility_Type(User user, League league, String stage, AbilityType abilityType);

    List<AbilityUsage> findByUser(User user);

    boolean existsByUserAndAbility_Code(User user, String code);
}