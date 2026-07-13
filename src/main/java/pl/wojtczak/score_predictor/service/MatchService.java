package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.repository.MatchRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
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
        if ("FINISHED".equals(existingMatch.getStatus())) {
            return;
        }
        existingMatch.setMatchDate(importedMatch.getMatchDate());
        existingMatch.setStatus(importedMatch.getStatus());
        existingMatch.setHomeScore(importedMatch.getHomeScore());
        existingMatch.setAwayScore(importedMatch.getAwayScore());
        matchRepository.save(existingMatch);
        }
}
