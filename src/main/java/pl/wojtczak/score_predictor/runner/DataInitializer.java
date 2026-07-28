package pl.wojtczak.score_predictor.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.wojtczak.score_predictor.dto.imports.MatchImportDto;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.Team;
import pl.wojtczak.score_predictor.repository.MatchRepository;
import pl.wojtczak.score_predictor.service.JsonFileService;
import pl.wojtczak.score_predictor.service.MatchImportService;
import pl.wojtczak.score_predictor.service.TeamImportService;
import pl.wojtczak.score_predictor.service.TeamService;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TeamService teamService;
    private final JsonFileService jsonFileService;
    private final MatchImportService matchImportService;

    private final MatchRepository matchRepository;
    private final TeamImportService teamImportService;

    public DataInitializer(TeamService teamService, JsonFileService jsonFileService,
                           MatchImportService matchImportService, MatchRepository matchRepository, TeamImportService teamImportService) {
        this.teamService = teamService;
        this.jsonFileService = jsonFileService;

        this.matchImportService = matchImportService;
        this.matchRepository = matchRepository;
        this.teamImportService = teamImportService;
    }

    @Override
    public void run(String... args) throws Exception {

        List<Team> teams = teamService.getAllTeams();
        if (teams.isEmpty()) {
            System.out.println("\nNo teams found in the database.");
        } else {
            System.out.println("\nTeams in the database:");
        }
        for (Team team : teams) {
            System.out.println(team.getName());
        }

        teamImportService.importTeams();
        matchImportService.importMatches();

    }
}
