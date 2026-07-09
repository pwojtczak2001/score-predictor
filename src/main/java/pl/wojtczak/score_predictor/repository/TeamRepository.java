package pl.wojtczak.score_predictor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wojtczak.score_predictor.entity.Team;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Integer>{

    boolean existsByName(String name);

    Optional<Team> findByName(String name);

}
