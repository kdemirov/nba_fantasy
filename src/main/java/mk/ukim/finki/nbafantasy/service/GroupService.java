package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.Group;

import java.util.List;

/**
 * Service for {@link Group}
 */
public interface GroupService {
    /**
     * Saves group with given name with creator user
     *
     * @param name     group name
     * @param username creator of the group
     * @return {@link Group}
     */
    Group save(String name, String username);

    /**
     * Finds group by given id.
     *
     * @param id given id
     * @return {@link  Group}
     */
    Group findById(Long id);

    /**
     * Updates the name of the group with the given id.
     *
     * @param id   given id
     * @param name updated name
     * @return {@link Group}
     */
    Group update(Long id, String name);

    /**
     * Invites user with given username to the group with the given id.
     *
     * @param username username of the invited user
     * @param groupId  given group id
     */
    void inviteUser(String username, Long groupId);

    /**
     * The invited user with username accepts the invitation with notification id and joins the
     * group with group id.
     *
     * @param groupId        given group id
     * @param notificationId notification id
     * @param username       invited user's username
     */
    void joinGroup(Long groupId, Long notificationId, String username);

    /**
     * The invited user with username declines the invitation with notification id and
     * the notification with notification id is deleted.
     *
     * @param groupId        given group id
     * @param notificationId notification id
     * @param username       invited user's username
     */
    void declineInvitedGroup(Long groupId, Long notificationId, String username);

    /**
     * Finds top 3 groups by points for user with given username.
     *
     * @param username given username
     * @return List of {@link Group}
     */
    List<Group> findAllGroupsByUser(String username);


}
