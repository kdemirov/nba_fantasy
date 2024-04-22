package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Jpa repository for {@link User}.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * Finds a user if exist with given username.
     *
     * @param username given username
     * @return {@link User}
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds list of users which contains the given player in their team.
     *
     * @param player given player
     * @return list of {@link User}
     */
    List<User> findAllByMyTeamContains(Player player);
}
