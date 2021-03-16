package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.Game;

import java.util.List;
import java.util.Map;

public interface GameService {
    void saveGames();
    List<Game> findAll();
    Game findById(Long id);
    Game update(Long id,Integer pointsHomeTeam,Integer pointsAwayTeam,String time,String gameDetailsUrl);
    void getGameDetails(Long id,String gameDetailsUrl);
    Map<String,List<Game>> findAllFinishedGames();
    Map<String,List<Game>> findAllUnfinishedGames();

}
