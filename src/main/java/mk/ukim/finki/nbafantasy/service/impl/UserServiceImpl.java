package mk.ukim.finki.nbafantasy.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.ConfirmationToken;
import mk.ukim.finki.nbafantasy.model.Notifications;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.dto.NotificationDto;
import mk.ukim.finki.nbafantasy.model.enumerations.Role;
import mk.ukim.finki.nbafantasy.model.exceptions.*;
import mk.ukim.finki.nbafantasy.repository.jpa.UserRepository;
import mk.ukim.finki.nbafantasy.service.ConfirmationTokenService;
import mk.ukim.finki.nbafantasy.service.PlayerService;
import mk.ukim.finki.nbafantasy.service.UserService;
import mk.ukim.finki.nbafantasy.service.mappers.NotificationMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PlayerService playerService;
    private final ConfirmationTokenService confirmationTokenService;
    private final NotificationMapper notificationMapper;

    @Override
    public User register(String username, String password, String email, String name, String surname, Role role) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()
                || email == null || email.isEmpty() || name == null || name.isEmpty()
                || surname == null || surname.isEmpty()) {
            throw new InvalidArgumentException();
        }
        if (this.userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistException(username);

        }
        User user = new User(username, this.passwordEncoder.encode(password), email, name, surname, role);
        return this.userRepository.save(user);

    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public Player addPlayer(String username, Long playerId) {
        Player player = this.playerService.findById(playerId);
        User user = findByUsername(username);
        if (user.getMyTeam().contains(player)) {
            throw new PlayerAlreadyExistException(player.getName());
        }
        if (player.getPosition().equals("C") || player.getPosition().equals("C-F")) {
            if (user.getCenterPlayer().isPresent()) {
                throw new CenterPlayerAlreadyExistException(user.getCenterPlayer().get().getName());
            }
        }
        if (player.getPosition().equals("F") || player.getPosition().equals("F-C")) {
            if (user.getForwardPlayers().size() == 2) {
                throw new ForwardPlayersAlreadyExistException();
            }
        }
        if (player.getPosition().equals("G") || player.getPosition().equals("G-F")) {
            if (user.getGuardPlayers().size() == 2) {
                throw new GuardPlayersAlreadyExistException();
            }
        }
        user.getMyTeam().add(player);
        this.userRepository.save(user);
        return player;
    }

    @Override
    public void deletePlayer(String username, Long id) {
        Player player = this.playerService.findById(id);
        User user = findByUsername(username);
        user.removePlayerFromMyTeam(player);
        this.userRepository.save(user);

    }

    @Override
    public boolean verifyAccount(String confirmationTokenString, User user) {
        ConfirmationToken confirmationToken = this.confirmationTokenService.findByUser(user);
        if (confirmationToken.getConfirmationToken().equals(confirmationTokenString)) {
            user.setEnabled(true);
            this.userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void deleteNotificatiton(Notifications notifications, String username) {
        User user = findByUsername(username);
        user.removeNotification(notifications);
        this.userRepository.save(user);
    }

    @Override
    public List<User> findAllUsersContainingPlayer(Player player) {
        return this.userRepository.findAllByMyTeamContains(player);
    }

    @Override
    public void calculateUsersFantasyPoints(Player player) {
        List<User> users = findAllUsersContainingPlayer(player);
        if (!users.isEmpty()) {
            users.forEach(u -> {
                u.calculateFantasyPoints(player.getFantasyPointPerGame());
                this.userRepository.save(u);
            });
        }
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public List<NotificationDto> fetchNotification(String username) {
        return findByUsername(username).getNotifications()
                .stream()
                .map(notificationMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public void resetWeeklyPoints() {
        this.userRepository.findAll().forEach(u -> {
            u.resetWeeklyPoint();
            this.userRepository.save(u);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(s));
    }
}
