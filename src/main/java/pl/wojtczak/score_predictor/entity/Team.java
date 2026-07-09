package pl.wojtczak.score_predictor.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Integer teamId;

    @Column(nullable = false, length = 21, unique = true)
    private String name;

    @Column(name = "display_name", length = 21, unique = true)
    private String displayName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "logo_url", nullable = false)
    private String logoUrl;

    public Team() {
    }

    public Team(String name, String logoUrl){
        this.name = name;
        this.logoUrl = logoUrl;
        this.createdAt = LocalDateTime.now();
    }

    public String getDisplayName() {
        return displayName;
    }

    public Integer getTeamId(){
        return teamId;
    }

    public String getName(){
        return name;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public String getLogoUrl(){
        return logoUrl;
    }

    public void setDisplayName(String newDisplayName) {
        this.displayName = newDisplayName;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setLogoUrl(String newLogoUrl) {
        this.logoUrl = newLogoUrl;
    }

    @Override
    public String toString() {
        return "\nTeam: " + name +
                " (team_id = " + teamId + ")" +
                "\nDisplay_name = '" + displayName + "'" +
                "\nCreated_at = " + createdAt +
                "\nLogo_url = '" + logoUrl + "'";
    }
}


