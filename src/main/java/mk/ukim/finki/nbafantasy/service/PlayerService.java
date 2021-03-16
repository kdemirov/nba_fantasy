package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.Game;
import mk.ukim.finki.nbafantasy.model.Player;

import java.util.List;

public interface PlayerService {
    void getPlayers();
    void fillPlayerImageUrl(Long id);
    List<Player>  findAll();
    Player findById(Long id);
    Player update(Long id,String name,Integer number,String height,String weightInLbs,String birthDate,Integer age,String expirience,String school,double price);
    Player findByName(String name);
    Player update(Long id,Integer personalFauls,Integer points,Integer minutesPlayed);
    void resetWeeklyPoints();
    void resetPointsPerGame(Game game);
}
