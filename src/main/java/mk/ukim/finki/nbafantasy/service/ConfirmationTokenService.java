package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.ConfirmationToken;
import mk.ukim.finki.nbafantasy.model.User;

/**
 * Service for {@link ConfirmationToken}.
 */
public interface ConfirmationTokenService {

    /**
     * Saves a confirmation token for given user.
     *
     * @param user given user
     * @return {@link ConfirmationToken}
     */
    ConfirmationToken save(User user);

    /**
     * Finds a confirmation token by given user.
     *
     * @param user given user
     * @return {@link ConfirmationToken}
     */
    ConfirmationToken findByUser(User user);
}
