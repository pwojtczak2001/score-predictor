package pl.wojtczak.score_predictor.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ability_usage")
public class AbilityUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ability_usage_id")
    private Integer abilityUsageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ability_id", nullable = false)
    private Ability ability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id")
    private League league;

    @Column(length = 30, updatable = false)
    private String stage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_match_id")
    private Match targetMatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    @Column(name = "activated_at", nullable = false, updatable = false)
    private LocalDateTime activatedAt;

    public AbilityUsage() {
    }

    public AbilityUsage(User user, Ability ability, League league, String stage, Match targetMatch, User targetUser) {
        this.user = user;
        this.ability = ability;
        this.league = league;
        this.stage = stage;
        this.targetMatch = targetMatch;
        this.targetUser = targetUser;
        this.activatedAt = LocalDateTime.now();
    }

    public Integer getAbilityUsageId() {
        return abilityUsageId;
    }

    public User getUser() {
        return user;
    }

    public Ability getAbility() {
        return ability;
    }

    public League getLeague() {
        return league;
    }

    public String getStage() {
        return stage;
    }

    public Match getTargetMatch() {
        return targetMatch;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public void setTargetMatch(Match targetMatch) {
        this.targetMatch = targetMatch;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

}
