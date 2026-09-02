package pl.wojtczak.score_predictor.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AbilityActivationRequest {

    @NotBlank
    private String abilityCode;
    private Integer leagueId;
    private String stage;
    private Integer targetMatchId;
    private Integer targetUserId;

    public AbilityActivationRequest() {
    }

    public AbilityActivationRequest(String abilityCode, Integer leagueId, Integer targetMatchId, Integer targetUserId, String stage) {
        this.abilityCode = abilityCode;
        this.leagueId = leagueId;
        this.targetMatchId = targetMatchId;
        this.targetUserId = targetUserId;
        this.stage = stage;
    }

    public String getAbilityCode() {
        return abilityCode;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public Integer getTargetMatchId() {
        return targetMatchId;
    }

    public Integer getTargetUserId() {
        return targetUserId;
    }

    public String getStage() {
        return stage;
    }

    public void setAbilityCode(String abilityCode) {
        this.abilityCode = abilityCode;
    }

    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    public void setTargetMatchId(Integer targetMatchId) {
        this.targetMatchId = targetMatchId;
    }

    public void setTargetUserId(Integer targetUserId) {
        this.targetUserId = targetUserId;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
