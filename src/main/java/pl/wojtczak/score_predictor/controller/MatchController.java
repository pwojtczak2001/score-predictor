package pl.wojtczak.score_predictor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wojtczak.score_predictor.dto.response.UpcomingMatchResponse;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.service.MatchService;
import pl.wojtczak.score_predictor.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private final MatchService matchService;

    private final UserService userService;

    @Autowired
    public MatchController(MatchService matchService, UserService userService) {
        this.matchService = matchService;
        this.userService = userService;
    }

    @GetMapping("/upcoming")
    public List<UpcomingMatchResponse> getUpcomingMatches() {
        User currentUser = userService.getCurrentUser();
        return matchService.getUpcomingMatches(currentUser);
    }

}
