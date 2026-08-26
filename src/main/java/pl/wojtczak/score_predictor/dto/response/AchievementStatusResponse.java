package pl.wojtczak.score_predictor.dto.response;

import java.time.LocalDateTime;

public class AchievementStatusResponse {

    private String code;
    private String name;
    private Integer coinsReward;
    private boolean unlocked;
    private LocalDateTime unlockedAt;

    public AchievementStatusResponse(String code, String name, Integer coinsReward, boolean unlocked, LocalDateTime unlockedAt) {
        this.code = code;
        this.name = name;
        this.coinsReward = coinsReward;
        this.unlocked = unlocked;
        this.unlockedAt = unlockedAt;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer getCoinsReward() {
        return coinsReward;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public LocalDateTime getUnlockedAt() {
        return unlockedAt;
    }

    void setCode(String code) {
        this.code = code;
    }

    void setName(String name) {
        this.name = name;
    }

    void setCoinsReward(Integer coinsReward) {
        this.coinsReward = coinsReward;
    }

    void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    void setUnlockedAt(LocalDateTime unlockedAt) {
        this.unlockedAt = unlockedAt;
    }

}
