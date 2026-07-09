package pl.wojtczak.score_predictor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.Team;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Integer> {

    boolean existsByExternalMatchId(String externalMatchId);

    Optional<Match> findByExternalMatchId(String externalMatchId);

}
