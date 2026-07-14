package pl.wojtczak.score_predictor.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class LeagueMember {


    @EmbeddedId
    private LeagueMemberId id;

    @ManyToOne
    @JoinColumn(name = "league_id", referencedColumnName = "league_id", nullable = false, updatable = false)
    @MapsId("leagueId")
    private League league;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, updatable = false)
    @MapsId("userId")
    private User user;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @Column(name = "current_points", nullable = false)
    private Integer currentPoints;

    public LeagueMember() {
    }

    public LeagueMember(League league, User user) {
        this.id = new LeagueMemberId(league.getLeagueId(), user.getUserId());
        this.league = league;
        this.user = user;
        this.joinedAt = LocalDateTime.now();
        this.currentPoints = 0;
    }

    public League getLeague() {
        return league;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public Integer getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(Integer currentPoints) {
        this.currentPoints = currentPoints;
    }
}
