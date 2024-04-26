package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.model.ConfirmationToken;
import mk.ukim.finki.nbafantasy.model.Notifications;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.enumerations.Role;
import mk.ukim.finki.nbafantasy.model.exceptions.*;
import mk.ukim.finki.nbafantasy.repository.jpa.UserRepository;
import mk.ukim.finki.nbafantasy.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Tests for UserService.
 */
class UserServiceTest extends AbstractTestClass {

    private static final String ENCODED_STRING = "2rdfggsassrr$%fsaa";
    private static final List<Player> PLAYER_LIST = createPlayers();
    private static final Integer ZERO_FANTASY_POINTS = 0;
    private static final Integer TEAMS_SIZE = 0;
    private static final Integer NOTIFICATIONS_SIZE = 0;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    PlayerService playerService;

    @Mock
    ConfirmationTokenService confirmationTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_register_user() {
        //given
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_STRING);
        USER.setPassword(ENCODED_STRING);
        when(userRepository.save(USER)).thenReturn(USER);

        //when
        User user = userService.register(USERNAME, PASSWORD, EMAIL, NAME, SURNAME, Role.ROLE_USER);

        //then
        Assertions.assertEquals(NAME, user.getName());
        Assertions.assertEquals(ENCODED_STRING, user.getPassword());
        Assertions.assertEquals(USERNAME, user.getUsername());
        Assertions.assertEquals(EMAIL, user.getEmail());
        Assertions.assertEquals(SURNAME, user.getSurname());
        Assertions.assertEquals(Role.ROLE_USER, user.getRole());
    }

    @Test
    void should_throw_exception_if_user_already_exist() {
        //given
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));

        //when
        Assert.assertThrows(UsernameAlreadyExistException.class,
                () -> this.userService.register(USERNAME, PASSWORD, EMAIL, NAME, SURNAME, Role.ROLE_USER));
    }

    @Test
    void should_throw_exception_if_any_of_the_arguments_are_empty_or_null() {
        //Check for username
        Assert.assertThrows(InvalidArgumentException.class,
                () -> this.userService.register(null, PASSWORD, EMAIL, NAME, SURNAME, Role.ROLE_USER));
        Assert.assertThrows(InvalidArgumentException.class,
                () -> this.userService.register("", PASSWORD, EMAIL, NAME, SURNAME, Role.ROLE_USER));

        //Check for password
        Assert.assertThrows(InvalidArgumentException.class,
                () -> this.userService.register(USERNAME, null, EMAIL, NAME, SURNAME, Role.ROLE_USER));
        Assert.assertThrows(InvalidArgumentException.class,
                () -> this.userService.register(USERNAME, "", EMAIL, NAME, SURNAME, Role.ROLE_USER));

        //Check for email
        Assert.assertThrows(InvalidArgumentException.class,
                () -> this.userService.register(USERNAME, PASSWORD, null, NAME, SURNAME, Role.ROLE_USER));
        Assert.assertThrows(InvalidArgumentException.class,
                () -> this.userService.register(USERNAME, PASSWORD, "", NAME, SURNAME, Role.ROLE_USER));

        //Check for name
        Assert.assertThrows(InvalidArgumentException.class,
                () -> this.userService.register(USERNAME, PASSWORD, EMAIL, null, SURNAME, Role.ROLE_USER));
        Assert.assertThrows(InvalidArgumentException.class,
                () -> this.userService.register(USERNAME, PASSWORD, EMAIL, "", SURNAME, Role.ROLE_USER));

        //Check for surname
        Assert.assertThrows(InvalidArgumentException.class,
                () -> this.userService.register(USERNAME, PASSWORD, EMAIL, NAME, null, Role.ROLE_USER));
        Assert.assertThrows(InvalidArgumentException.class,
                () -> this.userService.register(USERNAME, PASSWORD, EMAIL, NAME, "", Role.ROLE_USER));
    }

    @Test
    void should_save_user() {
        //given
        when(userRepository.save(USER)).thenReturn(USER);

        //when
        User user = userService.save(USER);

        //then
        Assertions.assertEquals(NAME, user.getName());
        Assertions.assertEquals(SURNAME, user.getSurname());
        Assertions.assertEquals(EMAIL, user.getEmail());
        Assertions.assertEquals(USERNAME, user.getUsername());
        Assertions.assertEquals(PASSWORD, user.getPassword());
        Assertions.assertEquals(Role.ROLE_USER, user.getRole());
    }

    @Test
    void should_find_user_by_username() {
        //given
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));

        //when
        User user = userService.findByUsername(USERNAME);

        //then
        Assertions.assertEquals(USER, user);
    }

    @Test
    void should_find_user_by_username_and_throw_exception() {
        //given
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername(USERNAME));
    }

    @Test
    void should_add_center_player() {
        //given
        Player centerPlayer = getCenterPlayer();
        when(this.playerService.findById(centerPlayer.getId())).thenReturn(centerPlayer);
        USER.setMyTeam(new ArrayList<>());
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));

        //when
        Player savedPlayer = userService.addPlayer(USERNAME, centerPlayer.getId());

        //then
        Assertions.assertEquals(centerPlayer.getId(), savedPlayer.getId());
        Assertions.assertEquals(centerPlayer.getName(), savedPlayer.getName());
        Assertions.assertEquals(centerPlayer.getPosition(), savedPlayer.getPosition());
    }

    @Test
    void should_throw_exception_when_player_already_exist() {
        //given
        Player centerPlayer = getCenterPlayer();
        when(this.playerService.findById(centerPlayer.getId())).thenReturn(centerPlayer);
        USER.setMyTeam(List.of(centerPlayer));
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));

        //then
        assertThrows(PlayerAlreadyExistException.class, () -> userService.addPlayer(USERNAME, centerPlayer.getId()));
    }

    @Test
    void should_throw_center_player_already_exist_exception() {
        //given
        Player centerPlayer = getCenterPlayer();
        USER.setMyTeam(List.of(centerPlayer));
        Player newCenterPlayer = new Player(NAME,
                PLAYER_NUMBER,
                CENTER_PLAYER_POSITION,
                HEIGHT,
                WEIGHT_LBS,
                BIRTH_DATE,
                AGE,
                EXPERIENCE,
                SCHOOL);
        newCenterPlayer.setId(ID);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        when(playerService.findById(newCenterPlayer.getId())).thenReturn(newCenterPlayer);

        //when
        assertThrows(CenterPlayerAlreadyExistException.class,
                () -> userService.addPlayer(USERNAME, newCenterPlayer.getId()));
    }

    @Test
    void should_add_forward_player() {
        //given
        Player forwardPlayer = getForwardPlayers().get(0);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        when(playerService.findById(forwardPlayer.getId())).thenReturn(forwardPlayer);

        //when
        Player savedPlayer = userService.addPlayer(USERNAME, forwardPlayer.getId());

        //then
        Assertions.assertEquals(forwardPlayer.getId(), savedPlayer.getId());
        Assertions.assertEquals(forwardPlayer.getName(), savedPlayer.getName());
        Assertions.assertEquals(forwardPlayer.getPosition(), savedPlayer.getPosition());
    }

    @Test
    void should_throw_exceptions_if_forward_players_exists() {
        //given
        List<Player> forwardPlayers = getForwardPlayers();
        USER.setMyTeam(forwardPlayers);
        Player forwardPlayer = new Player(NAME,
                PLAYER_NUMBER,
                FORWARD_PLAYER_POSITION,
                HEIGHT,
                WEIGHT_LBS,
                BIRTH_DATE,
                AGE,
                EXPERIENCE,
                SCHOOL);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        when(playerService.findById(forwardPlayer.getId())).thenReturn(forwardPlayer);

        //when
        assertThrows(ForwardPlayersAlreadyExistException.class,
                () -> userService.addPlayer(USERNAME, forwardPlayer.getId()));
    }

    @Test
    void should_add_guard_player() {
        //given
        Player guardPlayer = getGuardPlayers().get(0);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        when(playerService.findById(guardPlayer.getId())).thenReturn(guardPlayer);

        //when
        Player savedPlayer = userService.addPlayer(USERNAME, guardPlayer.getId());

        //then
        Assertions.assertEquals(guardPlayer.getId(), savedPlayer.getId());
        Assertions.assertEquals(guardPlayer.getName(), savedPlayer.getName());
        Assertions.assertEquals(guardPlayer.getPosition(), savedPlayer.getPosition());
    }

    @Test
    void should_throw_exceptions_if_guard_players_exists() {
        //given
        List<Player> guardPlayers = getGuardPlayers();
        USER.setMyTeam(guardPlayers);
        Player guardPlayer = new Player(NAME,
                PLAYER_NUMBER,
                GUARD_PLAYER_POSITION,
                HEIGHT,
                WEIGHT_LBS,
                BIRTH_DATE,
                AGE,
                EXPERIENCE,
                SCHOOL);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        when(playerService.findById(guardPlayer.getId())).thenReturn(guardPlayer);

        //when
        assertThrows(GuardPlayersAlreadyExistException.class,
                () -> userService.addPlayer(USERNAME, guardPlayer.getId()));
    }

    @Test
    void should_delete_player() {
        //given
        Player player = getCenterPlayer();
        player.setId(ID);
        USER.setMyTeam(List.of(player));
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        when(playerService.findById(player.getId())).thenReturn(player);

        //when
        userService.deletePlayer(USERNAME, player.getId());

        //then
        Assertions.assertEquals(TEAMS_SIZE, USER.getMyTeam().size());
    }

    @Test
    void should_verify_users_account() {
        //given
        ConfirmationToken confirmationToken = new ConfirmationToken(USER);
        String expectedConfirmationCode = confirmationToken.getConfirmationToken();
        when(this.confirmationTokenService.findByUser(USER)).thenReturn(confirmationToken);

        //when
        boolean expectedValue = userService.verifyAccount(expectedConfirmationCode, USER);

        //then
        Assertions.assertEquals(true, expectedValue);
        Assertions.assertEquals(true, USER.isEnabled());
    }

    @Test
    void should_not_verify_users_account() {
        //given
        ConfirmationToken confirmationToken = new ConfirmationToken(USER);
        USER.setEnabled(false);
        when(this.confirmationTokenService.findByUser(USER)).thenReturn(confirmationToken);

        //when
        boolean expectedValue = userService.verifyAccount(ENCODED_STRING, USER);

        //then
        Assertions.assertEquals(false, expectedValue);
        Assertions.assertEquals(false, USER.isEnabled());
    }

    @Test
    void should_delete_notification() {
        //given
        Notifications notification = new Notifications();
        notification.setId(ID);
        USER.setNotifications(List.of(notification));
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));

        //when
        userService.deleteNotification(notification, USERNAME);

        //then
        Assertions.assertEquals(NOTIFICATIONS_SIZE, USER.getNotifications().size());
    }

    @Test
    void calculateUsersFantasyPoints() {
        //given
        Player player = getCenterPlayer();
        player.setFantasyPointPerGame(FIVE_FANTASY_POINTS);
        player.setTotalFantasyPoints(ZERO_FANTASY_POINTS);
        player.setFantasyPointsWeekly(ZERO_FANTASY_POINTS);
        when(userRepository.findAllByMyTeamContains(player))
                .thenReturn(List.of(USER, TEST_USER));

        //when
        userService.calculateUsersFantasyPoints(player);

        //then
        Assertions.assertEquals(FIVE_FANTASY_POINTS, USER.getFantasyWeeklyPoints());
        Assertions.assertEquals(FIVE_FANTASY_POINTS, TEST_USER.getFantasyTotalPoints());
    }

    @Test
    void should_reset_weekly_points() {
        //given
        USER.setFantasyWeeklyPoints(FIVE_FANTASY_POINTS);
        TEST_USER.setFantasyWeeklyPoints(FIVE_FANTASY_POINTS);
        when(userRepository.findAll()).thenReturn(List.of(USER, TEST_USER));

        //when
        userService.resetWeeklyPoints();

        //then
        Assertions.assertEquals(ZERO_FANTASY_POINTS, USER.getFantasyWeeklyPoints());
        Assertions.assertEquals(ZERO_FANTASY_POINTS, TEST_USER.getFantasyWeeklyPoints());
    }

    private Player getCenterPlayer() {
        return PLAYER_LIST
                .stream().filter(p -> p.getPosition().equals(CENTER_PLAYER_POSITION))
                .findFirst()
                .orElse(null);
    }

    private List<Player> getForwardPlayers() {
        return PLAYER_LIST
                .stream().filter(p -> p.getPosition().equals(FORWARD_PLAYER_POSITION))
                .collect(Collectors.toList());
    }

    private List<Player> getGuardPlayers() {
        return PLAYER_LIST
                .stream().filter(p -> p.getPosition().equals(GUARD_PLAYER_POSITION))
                .collect(Collectors.toList());
    }
}