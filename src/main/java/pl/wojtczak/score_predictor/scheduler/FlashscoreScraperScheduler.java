package pl.wojtczak.score_predictor.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.wojtczak.score_predictor.service.FlashscoreScraperService;
import pl.wojtczak.score_predictor.service.MatchImportService;
import pl.wojtczak.score_predictor.service.TeamImportService;

import java.io.IOException;

@Component
public class FlashscoreScraperScheduler {

    private final FlashscoreScraperService flashscoreScraperService;

    private final TeamImportService teamImportService;

    private final MatchImportService matchImportService;

    public FlashscoreScraperScheduler(FlashscoreScraperService flashscoreScraperService, TeamImportService teamImportService, MatchImportService matchImportService) {
        this.flashscoreScraperService = flashscoreScraperService;
        this.teamImportService = teamImportService;
        this.matchImportService = matchImportService;
    }

    @Scheduled(fixedDelay = 15 * 60 * 1000)
    public void runScraper() throws IOException {
        flashscoreScraperService.runScraper();
        teamImportService.importTeams();
        matchImportService.importMatches();
    }

}
