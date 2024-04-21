package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.Group;
import mk.ukim.finki.nbafantasy.model.Notifications;

/**
 * Service for {@link Notifications}
 */
public interface NotificationService {

    /**
     * Finds a notification by given id.
     *
     * @param id given id
     * @return {@link Notifications}
     */
    Notifications findById(Long id);

    /**
     * Saves a notification with given group.
     *
     * @param group given group
     * @return {@link Notifications}
     */
    Notifications save(Group group);
}
