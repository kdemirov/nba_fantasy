package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.Game;
import mk.ukim.finki.nbafantasy.model.Player;

import java.util.List;

/**
 * Service for {@link Player}.
 */
public interface PlayerService {

    /**
     * Extract players for team with given id from parsed html document with given css class.
     *
     * @param teamId     given team id
     * @param tableClass given css class
     */
    void getPlayers(Long teamId, String tableClass);

    /**
     * Fills players image url from parsed document with given css class.
     *
     * @param className given css class
     */
    void fillPlayersImageUrl(String className);

    /**
     * Finds all players.
     *
     * @return list of {@link Player}
     */
    List<Player> findAll();

    /**
     * Finds a player by given id.
     *
     * @param id given id
     * @return {@link Player}
     */
    Player findById(Long id);

    /**
     * Updates a player by given id.
     *
     * @param id          given id
     * @param name        player's name
     * @param number      player's number
     * @param height      player's height
     * @param weightInLbs player's weight in lbs
     * @param birthDate   player's birthdate
     * @param age         player's age
     * @param experience  player's experience
     * @param school      player's school
     * @param price       player's price
     * @return {@link Player}
     */
    Player update(Long id,
                  String name,
                  Integer number,
                  String height,
                  String weightInLbs,
                  String birthDate,
                  Integer age,
                  String experience,
                  String school,
                  double price);

    Player findByName(String name);

    /**
     * Finds list of player which name starts with given string.
     *
     * @param name given string
     * @return List of {@link Player}
     */
    List<Player> findByNameStartsWith(String name);

    /**
     * Calculates fantasy points per game for player with given id
     * and extracted data from parsed document such as personal fouls,
     * points, minutes played.
     *
     * @param id            given id
     * @param personalFouls personal fouls
     * @param points        points
     * @param minutesPlayed minute played
     * @return updated {@link Player}
     */
    Player update(Long id, Integer personalFouls, Integer points, Integer minutesPlayed);

    /**
     * Resets weekly points for every player.
     */
    void resetWeeklyPoints();

    /**
     * Resets points per game.
     *
     * @param game given game
     */
    void resetPointsPerGame(Game game);
}
