package pl.wojtczak.score_predictor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wojtczak.score_predictor.dto.request.PredictionRequest;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.enums.PredictionOperationStatus;
import pl.wojtczak.score_predictor.service.MatchService;
import pl.wojtczak.score_predictor.service.PredictionService;
import pl.wojtczak.score_predictor.service.UserService;

@RestController
@RequestMapping("/predictions")
public class PredictionController {

    private final PredictionService predictionService;
    private final UserService userService;

    private final MatchService matchService;

    @Autowired
    public PredictionController(PredictionService predictionService, UserService userService, MatchService matchService) {
        this.predictionService = predictionService;
        this.userService = userService;
        this.matchService = matchService;
    }

    @PostMapping
    public PredictionOperationStatus createPrediction(@RequestBody PredictionRequest request){
        User currentUser = userService.getCurrentUser();
        Match match = matchService.getMatchByExternalId(request.getExternalMatchId());
        return predictionService.addPrediction(match, currentUser, request.getPredictedHomeScore(), request.getPredictedAwayScore());
    }

    @PutMapping
    public PredictionOperationStatus updatePrediction(@RequestBody PredictionRequest request){
        User currentUser = userService.getCurrentUser();
        Match match = matchService.getMatchByExternalId(request.getExternalMatchId());
        return predictionService.updatePrediction(match, currentUser, request.getPredictedHomeScore(), request.getPredictedAwayScore());
    }

}
