package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.league.LeagueRankingDto;
import pl.wojtczak.score_predictor.entity.League;
import pl.wojtczak.score_predictor.entity.LeagueMember;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.enums.LeagueJoinStatus;
import pl.wojtczak.score_predictor.repository.LeagueMemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeagueMemberService {

    private final LeagueMemberRepository leagueMemberRepository;

    public LeagueMemberService(LeagueMemberRepository leagueMemberRepository) {
        this.leagueMemberRepository = leagueMemberRepository;
    }

    public LeagueJoinStatus addUserToLeague(League league, User user) {
        if (leagueMemberRepository.existsByLeagueAndUser(league, user)) {
            return LeagueJoinStatus.USER_ALREADY_IN_LEAGUE;
        }

        LeagueMember leagueMember = new LeagueMember(league, user);
        leagueMemberRepository.save(leagueMember);
        return LeagueJoinStatus.SUCCESS;
    }

    public boolean isUserInLeague(League league, User user) {
        return leagueMemberRepository.existsByLeagueAndUser(league, user);
    }

    public List<LeagueMember> getAllMembersOfLeague(League league) {
        return leagueMemberRepository.findByLeague(league);
    }

    public List<LeagueMember> getMembershipsOfUser(User user) {
        return leagueMemberRepository.findByUser(user);
    }

    public List<LeagueRankingDto> getLeagueRanking(League league) {
        List<LeagueMember> members = leagueMemberRepository.findByLeagueOrderByCurrentPointsDesc(league);
        List<LeagueRankingDto> ranking = new ArrayList<>();

        int position = 1;

        for (LeagueMember member : members) {

            ranking.add(new LeagueRankingDto(
                    position++,
                    member.getUser().getUsername(),
                    member.getCurrentPoints()
            ));
        }
        return ranking;
    }
}
