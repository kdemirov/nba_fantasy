package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Jpa repository for {@link Game}.
 */
@Repository
public interface GameRepository extends JpaRepository<Game,Long> {
    List<Game> findAllByTimeEquals(String time);
    List<Game> findAllByTimeIsNotLike(String regex);
}
