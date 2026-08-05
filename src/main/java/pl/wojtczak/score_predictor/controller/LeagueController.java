package pl.wojtczak.score_predictor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wojtczak.score_predictor.dto.league.LeagueRankingDto;
import pl.wojtczak.score_predictor.service.LeagueService;

import java.util.List;

@RestController
@RequestMapping("/api/leagues")
public class LeagueController {

    private final LeagueService leagueService;

    @Autowired
    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping("/{leagueId}/ranking")
    public List<LeagueRankingDto> getLeagueRanking(@PathVariable Integer leagueId) {
        return leagueService.getLeagueRanking(leagueId);
    }

}
