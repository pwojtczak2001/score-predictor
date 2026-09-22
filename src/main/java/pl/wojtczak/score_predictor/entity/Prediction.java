package pl.wojtczak.score_predictor.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"match_id", "user_id", "league_id"})})
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prediction_id")
    private Integer predictionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", referencedColumnName = "match_id", nullable = false, updatable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", referencedColumnName = "league_id", nullable = false, updatable = false)
    private League league;

    @Column(name = "predicted_home_score", nullable = false, length = 2, columnDefinition = "INT CHECK (predicted_home_score >= 0 AND predicted_home_score <= 99)")
    private Integer predictedHomeScore;

    @Column(name = "predicted_away_score", nullable = false, length = 2, columnDefinition = "INT CHECK (predicted_away_score >= 0 AND predicted_away_score <= 99)")
    private Integer predictedAwayScore;

    @Column(name = "points_awarded", length = 3, columnDefinition = "INT CHECK (points_awarded >= 0 AND points_awarded <= 918)")
    private Integer pointsAwarded;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Prediction() {
    }

    public Prediction(Match match, User user, League league, Integer predictedHomeScore, Integer predictedAwayScore) {
        this.match = match;
        this.user = user;
        this.league = league;
        this.predictedHomeScore = predictedHomeScore;
        this.predictedAwayScore = predictedAwayScore;
        this.createdAt = LocalDateTime.now();
    }

    public Integer getPredictionId() {
        return predictionId;
    }

    public Match getMatch() {
        return match;
    }

    public User getUser() {
        return user;
    }

    public League getLeague() {
        return league;
    }

    public Integer getPredictedHomeScore() {
        return predictedHomeScore;
    }

    public Integer getPredictedAwayScore() {
        return predictedAwayScore;
    }

    public Integer getPointsAwarded() {
        return pointsAwarded;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setPredictedHomeScore(Integer predictedHomeScore) {
        this.predictedHomeScore = predictedHomeScore;
    }

    public void setPredictedAwayScore(Integer predictedAwayScore) {
        this.predictedAwayScore = predictedAwayScore;
    }

    public void setPointsAwarded(Integer pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }
}
