package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.entity.Match;
import pl.wojtczak.score_predictor.entity.Team;
import pl.wojtczak.score_predictor.repository.MatchRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }


    public void addMatch(String externalMatchId, String stage, LocalDateTime matchDate, String status, Team homeTeam, Team awayTeam, String source){
        if(matchRepository.existsByExternalMatchId(externalMatchId)){
            throw new IllegalArgumentException("Match with external ID '" + externalMatchId + "' already exists.");
        }
        Match match = new Match(externalMatchId, stage, matchDate, status, homeTeam, awayTeam, source);
        matchRepository.save(match);
    }

    public void addMatchIfNotExists(String externalMatchId, String stage, LocalDateTime matchDate, String status, Team homeTeam, Team awayTeam, String source){
        if(matchRepository.existsByExternalMatchId(externalMatchId)){
            return;
        }
        addMatch(externalMatchId, stage, matchDate, status, homeTeam, awayTeam, source);
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

}
