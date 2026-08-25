package pl.wojtczak.score_predictor.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserAchievementId implements Serializable{

    private Integer userId;
    private Integer achievementId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserAchievementId that = (UserAchievementId) o;

        return Objects.equals(userId, that.userId)
                && Objects.equals(achievementId, that.achievementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, achievementId);
    }

    public UserAchievementId() {
    }

    public UserAchievementId(Integer userId, Integer achievementId) {
        this.userId = userId;
        this.achievementId = achievementId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getAchievementId() {
        return achievementId;
    }

}
