package pl.wojtczak.score_predictor.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Integer matchId;

    @Column(nullable = false, name = "external_match_id", length = 30, unique = true, updatable = false)
    private String externalMatchId;

    @Column(length = 30, updatable = false)
    private String stage;

    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate;

    @Column(length = 30, nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", referencedColumnName = "team_id", nullable = false, updatable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", referencedColumnName = "team_id", nullable = false, updatable = false)
    private Team awayTeam;

    @Column(name = "home_score")
    private Integer homeScore;

    @Column(name = "away_score")
    private Integer awayScore;

    @Column(length = 30, nullable = false, updatable = false)
    private String source;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public Match() {
    }

    public Match(String externalMatchId, String stage, LocalDateTime matchDate, String status, Team homeTeam, Integer homeScore, Team awayTeam, Integer awayScore, String source) {
        this.externalMatchId = externalMatchId;
        this.stage = stage;
        this.matchDate = matchDate;
        this.status = status;
        this.homeTeam = homeTeam;
        this.homeScore = homeScore;
        this.awayTeam = awayTeam;
        this.awayScore = awayScore;
        this.source = source;
        this.createdAt = LocalDateTime.now();
    }

    public Integer getMatchId() {
        return matchId;
    }

    public String getExternalMatchId() {
        return externalMatchId;
    }

    public String getStage() {
        return stage;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public String getStatus() {
        return status;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public String getSource() {
        return source;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

}
