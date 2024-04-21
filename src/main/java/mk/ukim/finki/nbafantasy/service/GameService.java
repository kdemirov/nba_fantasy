package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.Game;

import java.util.List;
import java.util.Map;

/**
 * Service for {@link Game}
 */
public interface GameService {

    /**
     * Finds a game by given id.
     *
     * @param id given id
     * @return {@link Game}
     */
    Game findById(Long id);

    /**
     * Updates the points for each team in the game with given id,
     * the time, and game details url in order to be fetched the information
     * needed to calculate points for every player.
     *
     * @param id             given id
     * @param pointsHomeTeam points for home team
     * @param pointsAwayTeam points for away team
     * @param time           changed time to Final
     * @param gameDetailsUrl game details url
     * @return updated {@link Game}
     */
    Game update(Long id,
                Integer pointsHomeTeam,
                Integer pointsAwayTeam,
                String time,
                String gameDetailsUrl);

    /**
     * Calculates points per player if the game with the given id is finished.
     *
     * @param id given id
     */
    void getGameDetails(Long id);

    Game saveGame(String homeTeam,
                  String awayTeam,
                  String dayBegin,
                  String time,
                  String week,
                  String pointsHomeTeam,
                  String pointsAwayTeam,
                  String gameDetailsUrl);

    /**
     * Finds all finished games grouped by week.
     *
     * @return Map grouped by week with list of finished {@link Game}
     */
    Map<String, List<Game>> findAllFinishedGames();

    /**
     * Finds all unfinished games grouped by week
     *
     * @return Map grouped by week with list of unfinished {@link Game}
     */
    Map<String, List<Game>> findAllUnfinishedGames();
}
