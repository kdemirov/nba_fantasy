package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Jpa repository for {@link Player}.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    /**
     * Finds a player by name ignoring case.
     *
     * @param name given name
     * @return optional of {@link Player}
     */
    Optional<Player> findByNameLikeIgnoreCase(String name);

    /**
     * Finds list of players that start with given string.
     *
     * @param name given string
     * @return list of {@link Player}
     */
    List<Player> findByNameStartsWith(String name);
}
