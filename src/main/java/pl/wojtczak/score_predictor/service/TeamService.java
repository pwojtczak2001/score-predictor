package pl.wojtczak.score_predictor.service;

import org.springframework.stereotype.Service;
import pl.wojtczak.score_predictor.entity.Team;
import pl.wojtczak.score_predictor.exception.TeamAlreadyExistsException;
import pl.wojtczak.score_predictor.exception.TeamNotFoundException;
import pl.wojtczak.score_predictor.repository.TeamRepository;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public void addTeam(String name, String logoUrl){
        if(teamRepository.existsByName(name)){
            throw new TeamAlreadyExistsException(name);
        }
        Team team = new Team(name, logoUrl);
        teamRepository.save(team);
    }

    public void addTeamIfNotExists(String name, String logoUrl) {
        if(teamRepository.existsByName(name)){
            return;
        }
        addTeam(name, logoUrl);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamByName(String name) {
        return teamRepository.findByName(name)
                .orElseThrow(() ->
                        new TeamNotFoundException(name));
    }

}
