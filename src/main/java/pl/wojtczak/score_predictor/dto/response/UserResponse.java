package pl.wojtczak.score_predictor.dto.response;

import java.util.List;

public class UserResponse {

    private Integer userId;

    private String username;

    private String email;

    private Integer xp;
    private Integer coins;
    private Integer level;

    private List<AchievementStatusResponse> achievements;

    private long achievementsUnlockedCount;

    public UserResponse(Integer userId, String username, String email, Integer xp, Integer coins, Integer level, List<AchievementStatusResponse> achievements, long achievementsUnlockedCount) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.xp = xp;
        this.coins = coins;
        this.level = level;
        this.achievements = achievements;
        this.achievementsUnlockedCount = achievementsUnlockedCount;
    }

    public UserResponse() {
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Integer getXp() {
        return xp;
    }

    public Integer getCoins() {
        return coins;
    }

    public Integer getLevel() {
        return level;
    }

    public List<AchievementStatusResponse> getAchievements() {
        return achievements;
    }

    public long getAchievementsUnlockedCount() {
        return achievementsUnlockedCount;
    }

}
