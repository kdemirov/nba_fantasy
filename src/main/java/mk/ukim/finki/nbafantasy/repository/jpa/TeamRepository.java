package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.model.Team;
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
}
