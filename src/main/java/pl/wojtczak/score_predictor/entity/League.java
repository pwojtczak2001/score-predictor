package pl.wojtczak.score_predictor.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "league_id")
    private Integer leagueId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "invite_code", nullable = false, length = 6, unique = true)
    private String inviteCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false, updatable = false)
    private User createdBy;

    public League() {
    }

    public League(String name, String inviteCode, User createdBy) {
        this.name = name;
        this.inviteCode = inviteCode;
        this.createdAt = LocalDateTime.now();
        this.createdBy = createdBy;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public String getName() {
        return name;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
