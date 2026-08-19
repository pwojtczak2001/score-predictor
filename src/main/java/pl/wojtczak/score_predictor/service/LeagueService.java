package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.league.LeagueRankingDto;
import pl.wojtczak.score_predictor.dto.response.CreateLeagueResponse;
import pl.wojtczak.score_predictor.dto.response.MyLeagueResponse;
import pl.wojtczak.score_predictor.entity.League;
import pl.wojtczak.score_predictor.entity.LeagueMember;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.enums.LeagueJoinStatus;
import pl.wojtczak.score_predictor.exception.LeagueNotFoundException;
import pl.wojtczak.score_predictor.repository.LeagueMemberRepository;
import pl.wojtczak.score_predictor.repository.LeagueRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LeagueService {

    private final LeagueRepository leagueRepository;

    private final LeagueMemberService leagueMemberService;
    private final LeagueMemberRepository leagueMemberRepository;

    public LeagueService(LeagueRepository leagueRepository, LeagueMemberService leagueMemberService,
                         LeagueMemberRepository leagueMemberRepository) {
        this.leagueRepository = leagueRepository;
        this.leagueMemberService = leagueMemberService;
        this.leagueMemberRepository = leagueMemberRepository;
    }

    private String generateRandomInviteCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder inviteCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * characters.length());
            inviteCode.append(characters.charAt(index));
        }
        return inviteCode.toString();
    }

    private String generateUniqueInviteCode() {
        String inviteCode;
        do {
            inviteCode = generateRandomInviteCode();
        } while (leagueRepository.existsByInviteCode(inviteCode));
        return inviteCode;
    }

    public CreateLeagueResponse createLeague(String name, User owner) {
        String inviteCode = generateUniqueInviteCode();
        League league = new League(name, inviteCode, owner);
        leagueRepository.save(league);
        leagueMemberService.addUserToLeague(league, owner);
        return new CreateLeagueResponse(league.getLeagueId(), league.getName(), league.getInviteCode());
    }

    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    public LeagueJoinStatus joinLeague(String inviteCode, User user) {
        Optional<League> optionalLeague = leagueRepository.findByInviteCode(inviteCode);
        if (optionalLeague.isEmpty()) {
            return LeagueJoinStatus.LEAGUE_NOT_FOUND;
        }
        return leagueMemberService.addUserToLeague(optionalLeague.get(), user);
    }

    public List<LeagueRankingDto> getLeagueRanking(Integer leagueId) {
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new LeagueNotFoundException(leagueId));

        return leagueMemberService.getLeagueRanking(league);
    }

    public List<MyLeagueResponse> getLeaguesForUser(User currentUser) {
        List<LeagueMember> memberships = leagueMemberService.getMembershipsOfUser(currentUser);
        List<MyLeagueResponse> response = new ArrayList<>();
        for (LeagueMember membership : memberships) {
            League league = membership.getLeague();
            List<LeagueMember> members = leagueMemberRepository.findByLeagueOrderByCurrentPointsDesc(league);
            int position = 1;
            for (LeagueMember member : members) {
                if (member.getUser().getUserId().equals(currentUser.getUserId())) {
                    break;
                }
                position++;
            }
            Integer currentPoints = membership.getCurrentPoints();
            response.add(new MyLeagueResponse(
                    league.getLeagueId(),
                    league.getName(),
                    league.getInviteCode(),
                    leagueMemberRepository.countByLeague(league),
                    currentPoints,
                    position
            ));

        }
        return response;
    }
}
