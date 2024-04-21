package mk.ukim.finki.nbafantasy.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.Team;
import mk.ukim.finki.nbafantasy.model.exceptions.TeamDoesNotExistException;
import mk.ukim.finki.nbafantasy.repository.jpa.TeamRepository;
import mk.ukim.finki.nbafantasy.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public List<Team> getAll() {
        return this.teamRepository.findAll();
    }

    @Override
    public Team saveTeam(String conference,String name,String code,String playersUrl,String imageUrl) {
        Team team=new Team(conference,name,code,playersUrl,imageUrl);
        this.teamRepository.save(team);
        return team;
    }

    @Override
    public Player addPlayer(Long id,Player player) {
        Team team=findById(id);
        team.getPlayers().add(player);
        this.teamRepository.save(team);
        return player;
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
        Team tmpTeam = this.teamRepository.findById(team.getId()).orElseThrow(() -> new TeamDoesNotExistException(team.getId()));
        tmpTeam.setPlayers(team.getPlayers());
        return this.teamRepository.save(tmpTeam);
    }

    @Override
    public List<List<Team>> paginationTeams() {
        List<List<Team>> paginationTeam=new ArrayList<>();
        List<Team> tmp=new ArrayList<>();
        for(Team t:getAll()){
            if(tmp.size()==5){
                paginationTeam.add(tmp);
                tmp=new ArrayList<>();
            }
            tmp.add(t);
        }
        paginationTeam.add(tmp);
        return paginationTeam;
    }
}
