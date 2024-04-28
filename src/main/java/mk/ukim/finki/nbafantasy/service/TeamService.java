package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.Team;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service for {@link Team}
 */
public interface TeamService {
    /**
     * Finds all teams.
     *
     * @return List of {@link Team}
     */
    List<Team> getAll();

    /**
     * Saves a team with given properties extracted from parsed html document.
     *
     * @param conference team's conference or division
     * @param name       team's name
     * @param code       team's code
     * @param playersUrl url from where the players should be extracted for the team
     * @param imageUrl   team's image url
     * @return {@link Team}
     */
    Team saveTeam(String conference, String name, String code, String playersUrl, String imageUrl);

    /**
     * Finds a team by given id.
     *
     * @param id given id
     * @return {@link Team}
     * @throws {@link mk.ukim.finki.nbafantasy.model.exceptions.TeamDoesNotExistException}
     */
    Team findById(Long id);

    /**
     * Finds a team by given name.
     *
     * @param name team's name
     * @return {@link Team}
     * @throws {@link mk.ukim.finki.nbafantasy.model.exceptions.TeamDoesNotExistException}
     */
    Team findByName(String name);

    /**
     * Deletes a team by given id.
     *
     * @param id given id
     */
    void deleteTeam(Long id);

    /**
     * Updates a team with all players extracted from html parsed document
     *
     * @param team given team
     * @return {@link Team}
     */
    Team update(Team team);

    /**
     * Returns pagination of all teams each pagination is equal of 5 teams
     * needed for transfers.
     *
     * @return List which contains 5 {@link Team}
     */
    List<Team> paginationTeams(Pageable pageable);

    /**
     * Checks if a team exists with a given team name;
     *
     * @param teamName team name
     * @return true if exists otherwise false
     */
    Boolean exists(String teamName);
}
