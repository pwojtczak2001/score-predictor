package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.imports.MatchImportDto;
import pl.wojtczak.score_predictor.dto.imports.TeamImportDto;
import pl.wojtczak.score_predictor.entity.Team;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamImportService {

    private final TeamService teamService;
    private final JsonFileService jsonFileService;

    public TeamImportService(TeamService teamService, JsonFileService jsonFileService) {
        this.teamService = teamService;
        this.jsonFileService = jsonFileService;
    }

    private boolean isValidTeam(TeamImportDto team) {
        return team != null
                && team.getName() != null
                && !team.getName().isBlank()
                && team.getImage() != null
                && !team.getImage().isBlank();
    }

    public void importTeams() throws IOException {
        List<MatchImportDto> matches = jsonFileService.loadMatches();
        List<Team> teams = teamService.getAllTeams();

        Set<String> existingNames = new HashSet<>();

        for (Team team : teams) {
            existingNames.add(team.getName());
        }

        for (MatchImportDto matchImportDto: matches) {
            TeamImportDto homeTeam = matchImportDto.getHomeTeam();
            TeamImportDto awayTeam = matchImportDto.getAwayTeam();

            if (!isValidTeam(homeTeam) || !isValidTeam(awayTeam)) {
                System.out.println("Pomijam niepoprawny mecz: " + matchImportDto.getMatchId());
                continue;
            }

            if (existingNames.add(homeTeam.getName())) {
                teamService.addTeam(homeTeam.getName(), homeTeam.getImage());
            }

            if (existingNames.add(awayTeam.getName())) {
                teamService.addTeam(awayTeam.getName(), awayTeam.getImage());
            }

        }
    }
}
