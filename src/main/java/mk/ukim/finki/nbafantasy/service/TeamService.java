package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.Team;

import java.util.List;

public interface TeamService {
    List<Team> getAll();
    Team saveTeam(String conference,String name,String code,String playersUrl,String imageUrl);
    Player addPlayer(Long id,Player player);
    Team findById(Long id);
    Team findByName(String name);
    void getTeams();
    void deleteTeam(Long id);
    Team update(Team team);
    List<List<Team>> paginationTeams();
}
