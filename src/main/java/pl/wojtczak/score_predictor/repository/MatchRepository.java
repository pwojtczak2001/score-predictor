package pl.wojtczak.score_predictor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wojtczak.score_predictor.entity.Match;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Integer> {

    boolean existsByExternalMatchId(String externalMatchId);

    Optional<Match> findByExternalMatchId(String externalMatchId);

    List<Match> findByStatusOrderByMatchDateAsc(String status);

    Optional<Match> findFirstByStatusNotOrderByMatchDateAsc(String status);

    boolean existsByStageAndStatus(String stage, String status);

    long countByStage(String stage);

}
