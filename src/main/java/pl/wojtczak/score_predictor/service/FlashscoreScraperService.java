package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class FlashscoreScraperService {

    public void runScraper() {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "npm.cmd",
                "start",
                "--",
                "country=Poland",
                "league=Ekstraklasa",
                "season=2026-2027",
                "fileType=json",
                "concurrency=3"
        );

        processBuilder.directory(new File("flashscore_scraping"));
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            )) {
                String line;

                while ((line = reader.readLine()) != null) {

                    if (line.contains("Loading...")) {
                        continue;
                    }

                    if (line.isBlank()) {
                        continue;
                    }

                    System.out.println("[FLASHSCORE SCRAPER] " + line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException(
                        "Flashscore scraper failed with exit code: " + exitCode
                );
            }

            System.out.println(
                    "[FLASHSCORE SCRAPER] Process finished with exit code: " + exitCode
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to start Flashscore scraper.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Flashscore scraper process was interrupted.", e);
        }
    }
}