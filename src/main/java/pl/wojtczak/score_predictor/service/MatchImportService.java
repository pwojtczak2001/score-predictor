package pl.wojtczak.score_predictor.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.imports.MatchImportDto;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.Team;
import pl.wojtczak.score_predictor.exception.BadRequestException;

import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

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

    private void validateMatchImportData(
            MatchImportDto matchImportDto,
            Map<String, Team> teamsMap
    ) {

        if (matchImportDto.getMatchId() == null
                || matchImportDto.getMatchId().isBlank()) {

            throw new BadRequestException(
                    "Match ID is required"
            );
        }

        if (matchImportDto.getStage() == null
                || matchImportDto.getStage().isBlank()) {

            throw new BadRequestException(
                    "Stage is required for match '"
                            + matchImportDto.getMatchId() + "'"
            );
        }

        if (matchImportDto.getDate() == null
                || matchImportDto.getDate().isBlank()) {

            throw new BadRequestException(
                    "Date is required for match '"
                            + matchImportDto.getMatchId() + "'"
            );
        }

        try {
            LocalDateTime.parse(
                    matchImportDto.getDate(),
                    formatter
            );
        } catch (DateTimeParseException e) {
            throw new BadRequestException(
                    "Invalid date format for match '"
                            + matchImportDto.getMatchId()
                            + "'. Expected format: dd.MM.yyyy HH:mm"
            );
        }

        if (matchImportDto.getHomeTeam() == null
                || matchImportDto.getHomeTeam().getName() == null
                || matchImportDto.getHomeTeam().getName().isBlank()) {

            throw new BadRequestException(
                    "Home team is required for match '"
                            + matchImportDto.getMatchId() + "'"
            );
        }

        if (matchImportDto.getAwayTeam() == null
                || matchImportDto.getAwayTeam().getName() == null
                || matchImportDto.getAwayTeam().getName().isBlank()) {

            throw new BadRequestException(
                    "Away team is required for match '"
                            + matchImportDto.getMatchId() + "'"
            );
        }

        if (!teamsMap.containsKey(
                matchImportDto.getHomeTeam().getName()
        )) {

            throw new BadRequestException(
                    "Home team '"
                            + matchImportDto.getHomeTeam().getName()
                            + "' not found for match '"
                            + matchImportDto.getMatchId() + "'"
            );
        }

        if (!teamsMap.containsKey(
                matchImportDto.getAwayTeam().getName()
        )) {

            throw new BadRequestException(
                    "Away team '"
                            + matchImportDto.getAwayTeam().getName()
                            + "' not found for match '"
                            + matchImportDto.getMatchId() + "'"
            );
        }

        if (matchImportDto.getResult() == null) {

            throw new BadRequestException(
                    "Result is required for match '"
                            + matchImportDto.getMatchId() + "'"
            );
        }
    }

    @Transactional
    public void importMatches() throws IOException {

        List<MatchImportDto> matches = jsonFileService.loadMatches();
        List<Team> teams = teamService.getAllTeams();
        Map<String, Team> teamsMap = new HashMap<>();

        for (Team team : teams) {
            teamsMap.put(team.getName(), team);
        }

        for (MatchImportDto matchImportDto : matches) {
            validateMatchImportData(matchImportDto, teamsMap);
        }

        matches.sort(
                Comparator.comparing(
                        matchImportDto ->
                                LocalDateTime.parse(
                                        matchImportDto.getDate(),
                                        formatter
                                )
                )
        );

        Map<String, Match> existingMatchesMap = new HashMap<>();

        for (Match match : matchService.getAllMatches()){
            existingMatchesMap.put(match.getExternalMatchId(), match);
        }

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
                existingMatchesMap.remove(externalMatchId);

            } else {
                matchService.addMatch(importedMatch);
            }
        }

        for (Match match : existingMatchesMap.values()) {
            if (!"FINISHED".equals(match.getStatus())) {
                matchService.deleteMatch(match);
            }
        }
    }
}
