package pl.wojtczak.score_predictor.dto.imports;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MatchImportDto {

    private String matchId;
    private String stage;
    private String date;
    private String status;

    @JsonProperty("home")
    private TeamImportDto homeTeam;

    @JsonProperty("away")
    private TeamImportDto awayTeam;

    private ResultImportDto result;
    private List<InformationImportDto> information;

    public MatchImportDto() {
    }

    public String getMatchId() {
        return matchId;
    }

    public String getStage() {
        return stage;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public TeamImportDto getHomeTeam() {
        return homeTeam;
    }

    public TeamImportDto getAwayTeam() {
        return awayTeam;
    }

    public ResultImportDto getResult() {
        return result;
    }

    public List<InformationImportDto> getInformation() {
        return information;
    }

}
