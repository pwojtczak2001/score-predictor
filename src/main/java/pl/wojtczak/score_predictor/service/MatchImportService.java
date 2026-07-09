package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.imports.MatchImportDto;
import pl.wojtczak.score_predictor.entity.Team;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class MatchImportService {

    private static final String SOURCE = "Flashscore";
    private final JsonFileService jsonFileService;
    private final TeamService teamService;
    private final MatchService matchService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public MatchImportService(JsonFileService jsonFileService, TeamService teamService, MatchService matchService) {
        this.jsonFileService = jsonFileService;
        this.teamService = teamService;
        this.matchService = matchService;
    }

    public void importMatches() throws IOException {
        List<MatchImportDto> matches = jsonFileService.loadMatches();
        List<Team> teams = teamService.getAllTeams();
        Map<String, Team> teamsMap = new HashMap<>();

        for (Team team: teams) {
            teamsMap.put(team.getName(), team);
        }

        for (MatchImportDto matchImportDto: matches) {

            String externalMatchId = matchImportDto.getMatchId();
            String stage = matchImportDto.getStage();
            LocalDateTime matchDate = LocalDateTime.parse(matchImportDto.getDate(),formatter);
            String status = matchImportDto.getStatus();
            String source = SOURCE;

            Team homeTeam = teamsMap.get(matchImportDto.getHomeTeam().getName());
            Team awayTeam = teamsMap.get(matchImportDto.getAwayTeam().getName());

            matchService.addMatchIfNotExists(externalMatchId, stage, matchDate, status, homeTeam, awayTeam, source);
        }
    }

}
