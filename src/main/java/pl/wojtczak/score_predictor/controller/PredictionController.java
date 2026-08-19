package pl.wojtczak.score_predictor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PredictionOperationStatus> createPrediction(@RequestBody PredictionRequest request){
        User currentUser = userService.getCurrentUser();
        Match match = matchService.getMatchByExternalId(request.getExternalMatchId());
        PredictionOperationStatus status = predictionService.addPrediction(match, currentUser, request.getPredictedHomeScore(), request.getPredictedAwayScore());
        return switch (status) {
            case SUCCESS ->
                    ResponseEntity.status(HttpStatus.CREATED).body(status);

            case PREDICTION_ALREADY_EXISTS,
                    MATCH_ALREADY_STARTED ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(status);

            default ->
                    throw new IllegalStateException("Unexpected prediction status: " + status);
        };
    }

    @PutMapping
    public ResponseEntity<PredictionOperationStatus> updatePrediction(@RequestBody PredictionRequest request){
        User currentUser = userService.getCurrentUser();
        Match match = matchService.getMatchByExternalId(request.getExternalMatchId());
        PredictionOperationStatus status = predictionService.updatePrediction(match, currentUser, request.getPredictedHomeScore(), request.getPredictedAwayScore());
        return switch (status) {
            case SUCCESS ->
                    ResponseEntity.ok(status);

            case MATCH_ALREADY_STARTED ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(status);

            case PREDICTION_NOT_FOUND ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(status);

            default ->
                    throw new IllegalStateException("Unexpected prediction status: " + status);
        };
    }

}
