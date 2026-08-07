package pl.wojtczak.score_predictor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wojtczak.score_predictor.dto.league.LeagueRankingDto;
import pl.wojtczak.score_predictor.dto.request.CreateLeagueRequest;
import pl.wojtczak.score_predictor.dto.request.JoinLeagueRequest;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.enums.LeagueJoinStatus;
import pl.wojtczak.score_predictor.service.LeagueService;
import pl.wojtczak.score_predictor.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/leagues")
public class LeagueController {

    private final LeagueService leagueService;
    private final UserService userService;

    @Autowired
    public LeagueController(LeagueService leagueService, UserService userService) {
        this.leagueService = leagueService;
        this.userService = userService;
    }

    @GetMapping("/{leagueId}/ranking")
    public List<LeagueRankingDto> getLeagueRanking(@PathVariable Integer leagueId) {
        return leagueService.getLeagueRanking(leagueId);
    }

    @PostMapping
    public String createLeague(@RequestBody CreateLeagueRequest request) {
        User currentUser = userService.getCurrentUser();
        return leagueService.createLeague(request.getName(), currentUser);
    }

    @PostMapping("/join")
    public LeagueJoinStatus joinLeague(@RequestBody JoinLeagueRequest request) {
        User currentUser = userService.getCurrentUser();
        return leagueService.joinLeague(request.getInviteCode(), currentUser);
    }

}
