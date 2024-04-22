package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Jpa repository for {@link Game}.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    /**
     * Finds all games that their time equals like the given string
     *
     * @param time given string
     * @return List of {@link Game}
     */
    List<Game> findAllByTimeEquals(String time);

    /**
     * Finds all games where their time is not like the given regex.
     *
     * @param regex given regex
     * @return List of {@link Game}
     */
    List<Game> findAllByTimeIsNotLike(String regex);
}
