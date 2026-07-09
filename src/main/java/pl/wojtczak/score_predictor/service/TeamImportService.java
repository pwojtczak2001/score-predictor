package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.imports.MatchImportDto;
import pl.wojtczak.score_predictor.dto.imports.TeamImportDto;
import pl.wojtczak.score_predictor.repository.TeamRepository;

import java.io.IOException;
import java.util.List;

@Service
public class TeamImportService {

    private final TeamService teamService;
    private final JsonFileService jsonFileService;
    private final TeamRepository teamRepository;

    public TeamImportService(TeamService teamService, JsonFileService jsonFileService, TeamRepository teamRepository) {
        this.teamService = teamService;
        this.jsonFileService = jsonFileService;
        this.teamRepository = teamRepository;
    }

    public void importTeams() throws IOException {
        List<MatchImportDto> matches = jsonFileService.loadMatches();
        for (MatchImportDto matchImportDto: matches) {
            TeamImportDto homeTeam = matchImportDto.getHomeTeam();
            TeamImportDto awayTeam = matchImportDto.getAwayTeam();

            teamService.addTeamIfNotExists(homeTeam.getName(), homeTeam.getImage());
            teamService.addTeamIfNotExists(awayTeam.getName(), awayTeam.getImage());
        }
    }
}
