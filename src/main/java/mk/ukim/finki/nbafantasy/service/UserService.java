package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.Group;
import mk.ukim.finki.nbafantasy.model.Notifications;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.enumerations.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User register(String username, String password, String email, String name, String surname, Role role);
    User findByUsername(String username);
    Player addPlayer(String username,Long playerId);
    void deletePlayer(String username,Long id);
    boolean verifyAccount(String confirmationCode,User user);
    void update(String username,Group group);
    void deleteNotificatiton(Notifications notifications,String username);
    List<User> findAllUsersContainingPlayer(Player player);
    void calculateUsersFantasyPoints(Player player);
    List<User> findAll();
    void resetWeeklyPoints();
}
