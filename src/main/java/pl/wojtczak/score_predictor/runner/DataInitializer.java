package pl.wojtczak.score_predictor.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.wojtczak.score_predictor.dto.imports.MatchImportDto;
import pl.wojtczak.score_predictor.entity.Team;
import pl.wojtczak.score_predictor.repository.MatchRepository;
import pl.wojtczak.score_predictor.repository.TeamRepository;
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

    private final TeamImportService teamImportService;

    public DataInitializer(TeamService teamService, JsonFileService jsonFileService,
                           MatchImportService matchImportService, TeamImportService teamImportService) {
        this.teamService = teamService;
        this.jsonFileService = jsonFileService;

        this.matchImportService = matchImportService;
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

        List<MatchImportDto> matches = jsonFileService.loadMatches();
        String home = matches.get(0).getHomeTeam().getName();
        System.out.println("\n" + home + "\n");

        teamImportService.importTeams();

        Team team = teamService.getTeamByName("Pogon Szczecin");
        System.out.println(team + "\n");

        matchImportService.importMatches();

    }
}
