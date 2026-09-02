package pl.wojtczak.score_predictor.entity;

import jakarta.persistence.*;
import pl.wojtczak.score_predictor.enums.AbilityType;

import java.time.LocalDateTime;

@Entity
public class Ability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ability_id")
    private Integer abilityId;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "unlock_level", nullable = false, columnDefinition = "INT CHECK (unlock_level >= 1 AND unlock_level <= 10)")
    private Integer unlockLevel;

    @Column(name = "price_coins", nullable = false, columnDefinition = "INT CHECK (price_coins >= 0 AND price_coins <= 100)")
    private Integer priceCoins;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7)
    private AbilityType type;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Ability() {
    }

    public Ability(String code, String name, Integer unlockLevel, Integer priceCoins, AbilityType type) {
        this.code = code;
        this.name = name;
        this.unlockLevel = unlockLevel;
        this.priceCoins = priceCoins;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }

    public Integer getAbilityId() {
        return abilityId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public Integer getUnlockLevel() {
        return unlockLevel;
    }

    public Integer getPriceCoins() {
        return priceCoins;
    }

    public AbilityType getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnlockLevel(Integer unlockLevel) {
        this.unlockLevel = unlockLevel;
    }

    public void setPriceCoins(Integer priceCoins) {
        this.priceCoins = priceCoins;
    }

    public void setType(AbilityType type) {
        this.type = type;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
