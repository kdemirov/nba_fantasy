package mk.ukim.finki.nbafantasy.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.Team;
import mk.ukim.finki.nbafantasy.model.exceptions.TeamDoesNotExistException;
import mk.ukim.finki.nbafantasy.repository.jpa.TeamRepository;
import mk.ukim.finki.nbafantasy.service.TeamService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public List<Team> getAll() {
        return this.teamRepository.findAll();
    }

    @Override
    public Team saveTeam(String conference, String name, String code, String playersUrl, String imageUrl) {
        Team team = new Team(conference, name, code, playersUrl, imageUrl);
        this.teamRepository.save(team);
        return team;
    }

    @Override
    public Team findById(Long id) {
        return this.teamRepository.findById(id).orElseThrow(() -> new TeamDoesNotExistException(id));
    }

    @Override
    public Team findByName(String name) {
        return this.teamRepository.findByNameIgnoreCase(name).orElseThrow(() -> new TeamDoesNotExistException(name));
    }

    @Override
    public void deleteTeam(Long id) {
        this.teamRepository.deleteById(id);
    }

    @Override
    public Team update(Team team) {
        Team tmpTeam = findById(team.getId());
        tmpTeam.setPlayers(team.getPlayers());
        return this.teamRepository.save(tmpTeam);
    }

    @Override
    public List<Team> paginationTeams(Pageable pageable) {
        return this.teamRepository.findAll(pageable)
                .get()
                .collect(Collectors.toList());
    }

    @Override
    public Boolean exists(String teamName) {
        return teamRepository.existsByName(teamName);
    }
}
