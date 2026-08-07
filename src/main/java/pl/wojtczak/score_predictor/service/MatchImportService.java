package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.imports.MatchImportDto;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.Team;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

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
        Map<String, Match> existingMatchesMap = new HashMap<>();
        List<Team> teams = teamService.getAllTeams();
        Map<String, Team> teamsMap = new HashMap<>();

        for (Team team : teams) {
            teamsMap.put(team.getName(), team);
        }

        for (Match match : matchService.getAllMatches()){
            existingMatchesMap.put(match.getExternalMatchId(), match);
        }

        // TODO: Validate imported match data before parsing (date, teams, result)

        for (MatchImportDto matchImportDto : matches) {

            String externalMatchId = matchImportDto.getMatchId();
            String stage = matchImportDto.getStage();
            LocalDateTime matchDate = LocalDateTime.parse(matchImportDto.getDate(), formatter);
            String status = matchImportDto.getStatus();

            if (status == null || status.isBlank()) {
                status = "NOT STARTED";
            }

            String homeScoreString = matchImportDto.getResult().getHome();
            String awayScoreString = matchImportDto.getResult().getAway();

            Integer homeScore = null;
            if (homeScoreString != null) {
                homeScore = Integer.parseInt(homeScoreString);
            }

            Integer awayScore = null;
            if (awayScoreString != null) {
                awayScore = Integer.parseInt(awayScoreString);
            }

            Team homeTeam = teamsMap.get(matchImportDto.getHomeTeam().getName());
            Team awayTeam = teamsMap.get(matchImportDto.getAwayTeam().getName());

            Match importedMatch = new Match(externalMatchId, stage, matchDate, status, homeTeam, homeScore, awayTeam, awayScore, SOURCE);
            Optional<Match> existingMatch = Optional.ofNullable(existingMatchesMap.get(externalMatchId));

            if (existingMatch.isPresent()) {
                matchService.synchronizeMatch(existingMatch.get(), importedMatch);

            } else {
                matchService.addMatch(importedMatch);
            }
        }
    }
}
