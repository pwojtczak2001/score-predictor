package pl.wojtczak.score_predictor.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.wojtczak.score_predictor.service.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MatchImportService matchImportService;
    private final TeamImportService teamImportService;


    public DataInitializer(MatchImportService matchImportService, TeamImportService teamImportService) {
        this.matchImportService = matchImportService;
        this.teamImportService = teamImportService;
    }

    @Override
    public void run(String... args) throws Exception {
//        teamImportService.importTeams();
//        matchImportService.importMatches();
    }
}
