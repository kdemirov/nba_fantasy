package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.model.ConfirmationToken;
import mk.ukim.finki.nbafantasy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Jpa repository for {@link ConfirmationToken}.
 */
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    /**
     * Finds confirmation token by user in order to verify his account.
     *
     * @param user logged in user
     * @return optional of {@link ConfirmationToken}
     */
    Optional<ConfirmationToken> findByUser(User user);
}
