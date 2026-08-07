package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.dto.response.UpcomingMatchResponse;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.Prediction;
import pl.wojtczak.score_predictor.entity.User;
import pl.wojtczak.score_predictor.repository.MatchRepository;
import pl.wojtczak.score_predictor.repository.PredictionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final ScoringService scoringService;
    private final PredictionRepository predictionRepository;

    public MatchService(MatchRepository matchRepository, ScoringService scoringService, PredictionRepository predictionRepository) {
        this.matchRepository = matchRepository;
        this.scoringService = scoringService;
        this.predictionRepository = predictionRepository;
    }


    public void addMatch(Match match){
        if(matchRepository.existsByExternalMatchId(match.getExternalMatchId())){
            throw new IllegalArgumentException("Match with external ID '" + match.getExternalMatchId() + "' already exists.");
        }
        matchRepository.save(match);
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Match getMatchByExternalId(String externalMatchId) {
        return matchRepository.findByExternalMatchId(externalMatchId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Match with external ID '" + externalMatchId + "' not found. " +
                                        "The state of the application is inconsistent with our business assumptions."));
    }

    public Optional<Match> findMatchByExternalMatchId(String externalMatchId) {
        return matchRepository.findByExternalMatchId(externalMatchId);
    }

    public void synchronizeMatch(Match existingMatch, Match importedMatch) {
        boolean matchJustFinished =
                !"FINISHED".equals(existingMatch.getStatus())
                        && "FINISHED".equals(importedMatch.getStatus());

        if ("FINISHED".equals(existingMatch.getStatus())) {
            return;
        }

        existingMatch.setMatchDate(importedMatch.getMatchDate());
        existingMatch.setStatus(importedMatch.getStatus());
        existingMatch.setHomeScore(importedMatch.getHomeScore());
        existingMatch.setAwayScore(importedMatch.getAwayScore());
        matchRepository.save(existingMatch);


        if (matchJustFinished) {
            scoringService.calculateAndAwardPoints(existingMatch);
        }
    }

    public List<UpcomingMatchResponse> getUpcomingMatches(User currentUser){
        List<UpcomingMatchResponse> upcomingMatchesResponse = new ArrayList<>();
        List<Match> upcomingMatches = matchRepository.findByStatusOrderByMatchDateAsc("NOT STARTED");
        for (Match match : upcomingMatches) {
            Optional<Prediction> prediction = predictionRepository.findByMatchAndUser(match, currentUser);
            upcomingMatchesResponse.add(new UpcomingMatchResponse(
                    match.getExternalMatchId(),
                    match.getHomeTeam().getName(),
                    match.getAwayTeam().getName(),
                    match.getStage(),
                    match.getHomeTeam().getLogoUrl(),
                    match.getAwayTeam().getLogoUrl(),
                    match.getMatchDate(),
                    match.getStatus(),
                    prediction.map(Prediction::getPredictedHomeScore).orElse(null),
                    prediction.map(Prediction::getPredictedAwayScore).orElse(null)
            ));
        }
        return upcomingMatchesResponse;
    }
}
