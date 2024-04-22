package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.Notifications;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.dto.NotificationDto;
import mk.ukim.finki.nbafantasy.model.enumerations.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Service for {@link User}.
 */
public interface UserService extends UserDetailsService {

    /**
     * Registers an user with given properties
     *
     * @param username user's username
     * @param password user's password
     * @param email    user's email
     * @param name     user's name
     * @param surname  user's surname
     * @param role     user's role
     * @return {@link User}
     */
    User register(String username, String password, String email, String name, String surname, Role role);

    /**
     * Saves an updated user.
     *
     * @param user updated user
     * @return {@link User}
     */
    User save(User user);

    /**
     * Finds an user by given username.
     *
     * @param username given username
     * @return {@link User}
     * @throws {@link org.springframework.security.core.userdetails.UsernameNotFoundException}
     */
    User findByUsername(String username);

    /**
     * Adds a player with given id in the user's team
     *
     * @param username user's username
     * @param playerId given player id
     * @return {@link Player}
     */
    Player addPlayer(String username, Long playerId);

    /**
     * Removes a player with given id from user's team.
     *
     * @param username user's username
     * @param id       given player id.
     */
    void deletePlayer(String username, Long id);

    /**
     * Verifies an account for the given user with given confirmation code.
     *
     * @param confirmationCode confirmation code received from email
     * @param user             given user
     * @return true if confirmation code equals with the one send to the user's registered email
     */
    boolean verifyAccount(String confirmationCode, User user);

    /**
     * Deletes user's notification whether they accept or decline the invitation to join the group.
     *
     * @param notification given notification
     * @param username     user's username
     */
    void deleteNotification(Notifications notification, String username);

    /**
     * Finds all users which their team contains the given player.
     *
     * @param player given player
     * @return List of {@link User}
     */
    List<User> findAllUsersContainingPlayer(Player player);

    /**
     * Calculates fantasy points for users which their team contains the given player.
     *
     * @param player given player
     */
    void calculateUsersFantasyPoints(Player player);

    /**
     * Fetches notification every minute for an user with given username.
     *
     * @param username user's username
     * @return List of {@link NotificationDto}
     */
    List<NotificationDto> fetchNotification(String username);

    /**
     * Resets weekly points for all users.
     */
    void resetWeeklyPoints();
}
