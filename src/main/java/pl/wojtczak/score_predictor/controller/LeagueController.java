package pl.wojtczak.score_predictor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wojtczak.score_predictor.dto.league.LeagueRankingDto;
import pl.wojtczak.score_predictor.dto.request.CreateLeagueRequest;
import pl.wojtczak.score_predictor.dto.request.JoinLeagueRequest;
import pl.wojtczak.score_predictor.dto.response.CreateLeagueResponse;
import pl.wojtczak.score_predictor.dto.response.MyLeagueResponse;
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
    public ResponseEntity<List<LeagueRankingDto>> getLeagueRanking(@PathVariable Integer leagueId) {
        return ResponseEntity.ok(leagueService.getLeagueRanking(leagueId));
    }

    @PostMapping
    public ResponseEntity<CreateLeagueResponse> createLeague(@RequestBody CreateLeagueRequest request) {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(leagueService.createLeague(request.getName(), currentUser));
    }

    @PostMapping("/join")
    public ResponseEntity<LeagueJoinStatus> joinLeague(@RequestBody JoinLeagueRequest request) {
        User currentUser = userService.getCurrentUser();
        LeagueJoinStatus status = leagueService.joinLeague(request.getInviteCode(), currentUser);
        return switch (status) {
            case SUCCESS ->
                    ResponseEntity.ok(status);

            case LEAGUE_NOT_FOUND ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(status);


            case USER_ALREADY_IN_LEAGUE ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(status);

            default ->
                    throw new IllegalStateException("Unexpected league join status: " + status);
        };
    }

    @GetMapping("/my")
    public ResponseEntity<List<MyLeagueResponse>> getMyLeagues() {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(leagueService.getLeaguesForUser(currentUser));
    }

}
