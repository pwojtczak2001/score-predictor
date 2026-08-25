package pl.wojtczak.score_predictor.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achievement_id")
    private Integer achievementId;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "coins_reward", nullable = false)
    private Integer coinsReward;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Achievement() {
    }

    public Achievement(String code, String name, Integer coinsReward) {
        this.code = code;
        this.name = name;
        this.coinsReward = coinsReward;
        this.createdAt = LocalDateTime.now();
    }

    public Integer getAchievementId() {
        return achievementId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}