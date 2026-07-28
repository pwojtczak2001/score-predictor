package pl.wojtczak.score_predictor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wojtczak.score_predictor.entity.League;
import pl.wojtczak.score_predictor.entity.LeagueMember;
import pl.wojtczak.score_predictor.entity.LeagueMemberId;
import pl.wojtczak.score_predictor.entity.User;

import java.util.List;
import java.util.Set;

public interface LeagueMemberRepository extends JpaRepository<LeagueMember, LeagueMemberId> {

    List<LeagueMember> findByLeague(League league);
    List<LeagueMember> findByUser(User user);
    boolean existsByLeagueAndUser(League league, User user);

    List<LeagueMember> findByUserIn(Set<User> users);

    List<LeagueMember> findByLeagueOrderByCurrentPointsDesc(League league);

}
