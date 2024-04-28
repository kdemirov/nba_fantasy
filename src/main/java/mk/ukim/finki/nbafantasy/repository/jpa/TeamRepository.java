package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Jpa repository for {@link Team}.
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * Finds a team with given name ignoring case.
     *
     * @param name given name
     * @return optional of {@link Team}
     */
    Optional<Team> findByNameIgnoreCase(String name);

    /**
     * Returns page with {@link Team}.
     *
     * @param pageable given page request params page number and number of teams
     * @return Page of {@link Team}
     */
    Page<Team> findAll(Pageable pageable);

    /**
     * Checks if a team exists with a given name.
     *
     * @param teamName team name
     * @return true if exists otherwise false
     */
    Boolean existsByName(String teamName);
}
