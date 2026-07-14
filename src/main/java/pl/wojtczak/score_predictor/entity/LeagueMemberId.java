package pl.wojtczak.score_predictor.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LeagueMemberId implements Serializable {

    private Integer leagueId;

    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeagueMemberId that = (LeagueMemberId) o;

        return Objects.equals(leagueId, that.leagueId)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leagueId, userId);
    }

    public LeagueMemberId() {
    }

    public LeagueMemberId(Integer leagueId, Integer userId) {
        this.leagueId = leagueId;
        this.userId = userId;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public Integer getUserId() {
        return userId;
    }

}
