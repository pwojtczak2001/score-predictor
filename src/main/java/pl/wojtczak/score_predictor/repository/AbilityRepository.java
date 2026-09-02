package pl.wojtczak.score_predictor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wojtczak.score_predictor.entity.Ability;

import java.util.Optional;

public interface AbilityRepository extends JpaRepository<Ability, Integer> {

    Optional<Ability> findByCode(String code);

}