package pl.wojtczak.score_predictor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wojtczak.score_predictor.entity.League;

import java.util.Optional;

public interface LeagueRepository extends JpaRepository<League, Integer> {

    boolean existsByInviteCode(String inviteCode);

    Optional<League> findByInviteCode(String inviteCode);

}
