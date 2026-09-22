package pl.wojtczak.score_predictor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wojtczak.score_predictor.dto.request.AbilityActivationRequest;
import pl.wojtczak.score_predictor.dto.request.SpyTargetRequest;
import pl.wojtczak.score_predictor.dto.response.SpyPredictionResponse;
import pl.wojtczak.score_predictor.dto.response.SpyTargetUserResponse;
import pl.wojtczak.score_predictor.service.AbilityService;

import java.util.List;

@RestController
@RequestMapping("/abilities")
public class AbilityController {

    private final AbilityService abilityService;

    public AbilityController(AbilityService abilityService) {
        this.abilityService = abilityService;
    }

    @PostMapping("/activate")
    public ResponseEntity<Void> activateAbility(@RequestBody AbilityActivationRequest request) {

        abilityService.activateAbility(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/spy/targets")
    public ResponseEntity<List<SpyTargetUserResponse>> getSpyTargetUsers(
            @RequestParam Integer leagueId,
            @RequestParam String stage
    ) {
        return ResponseEntity.ok(abilityService.getSpyTargetUsers(leagueId, stage));
    }

    @PostMapping("/spy/target")
    public ResponseEntity<Void> selectSpyTarget(
            @RequestBody SpyTargetRequest request
    ) {
        abilityService.selectSpyTarget(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/spy/predictions")
    public ResponseEntity<List<SpyPredictionResponse>> getSpyPredictions(
            @RequestParam Integer leagueId,
            @RequestParam String stage
    ) {
        return ResponseEntity.ok(
                abilityService.getSpyPredictions(
                        leagueId,
                        stage
                )
        );
    }

}
