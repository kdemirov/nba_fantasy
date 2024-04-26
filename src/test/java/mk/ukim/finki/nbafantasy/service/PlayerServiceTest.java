package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.exceptions.PlayerDoesNotExistException;
import mk.ukim.finki.nbafantasy.repository.jpa.PlayerRepository;
import mk.ukim.finki.nbafantasy.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Player Service Test
 */
class PlayerServiceTest extends AbstractTestClass {

    private static final Integer NUMBER_OF_PLAYERS = 5;
    private static final Double PLAYER_PRICE = 420.0;
    private static final Integer TWO_FANTASY_WEEKLY_POINTS = 2;
    private static final Integer POINTS = 30;
    private static final Integer PERSONAL_FOULS = 3;
    private static final Integer MINUTES_PLAYED = 24;

    @InjectMocks
    PlayerServiceImpl playerService;

    @Mock
    TeamService teamService;

    @Mock
    PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_find_all_players() {
        //given
        when(playerRepository.findAll()).thenReturn(createPlayers());

        //when
        List<Player> players = playerService.findAll();

        //then
        Assertions.assertEquals(NUMBER_OF_PLAYERS, players.size());
    }

    @Test
    void should_find_player_by_id() {
        //given
        when(playerRepository.findById(any())).thenReturn(Optional.of(createPlayers().get(0)));

        //when
        Player player = playerService.findById(ID);

        //then
        Assertions.assertEquals(createPlayers().get(0).getName(), player.getName());
        Assertions.assertEquals(createPlayers().get(0).getPosition(), player.getPosition());
    }

    @Test
    void should_not_find_player_by_id() {
        //given
        when(playerRepository.findById(any())).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(PlayerDoesNotExistException.class,
                () -> playerService.findById(ID));
    }

    @Test
    void update() {
        //given
        when(playerRepository.findById(ID)).thenReturn(Optional.of(createPlayers().get(0)));

        //when
        Player player = playerService.update(ID,
                CENTER_PLAYER,
                PLAYER_NUMBER,
                HEIGHT,
                WEIGHT_LBS,
                BIRTH_DATE,
                AGE,
                EXPERIENCE,
                SCHOOL,
                PLAYER_PRICE);

        //then
        assertEquals(CENTER_PLAYER, player.getName());
        assertEquals(HEIGHT, player.getHeight());
        assertEquals(WEIGHT_LBS, player.getWeightInLbs());
        assertEquals(AGE, player.getAge());
        assertEquals(EXPERIENCE, player.getExperience());
        assertEquals(PLAYER_NUMBER, player.getNumber());
        assertEquals(BIRTH_DATE, player.getBirthDate());
        assertEquals(SCHOOL, player.getSchool());
        assertEquals(PLAYER_PRICE, player.getPrice());
    }

    @Test
    void should_find_player_by_name() {
        //given
        when(playerRepository.findByNameLikeIgnoreCase(CENTER_PLAYER)).thenReturn(Optional.of(createPlayers().get(0)));

        //when
        Player player = playerService.findByName(CENTER_PLAYER);

        //then
        Assertions.assertEquals(CENTER_PLAYER, player.getName());
        Assertions.assertEquals(CENTER_PLAYER_POSITION, player.getPosition());
    }

    @Test
    void should_not_find_player_by_name() {
        //given
        when(playerRepository.findByNameLikeIgnoreCase(CENTER_PLAYER_POSITION)).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(PlayerDoesNotExistException.class,
                () -> playerService.findByName(CENTER_PLAYER));
    }

    @Test
    void should_find_list_of_all_player_that_their_name_starts_with_given_string() {
        when(playerRepository.findByNameStartsWith(CENTER_PLAYER)).thenReturn(List.of(createPlayers().get(0)));

        List<Player> players = playerService.findByNameStartsWith(CENTER_PLAYER);

        assertEquals(1, players.size());
    }

    @Test
    void should_calculate_player_fantasy_points_per_GAME() {
        Player player = createPlayers().get(0);
        player.setFantasyPointsWeekly(TWO_FANTASY_WEEKLY_POINTS);
        when(playerRepository.findById(any())).thenReturn(Optional.of(player));

        //when
        Player updatedPlayer = playerService.update(ID, PERSONAL_FOULS, POINTS, MINUTES_PLAYED);

        //then
        Assertions.assertEquals(3, updatedPlayer.getFantasyPointPerGame());
        Assertions.assertEquals(5, updatedPlayer.getFantasyPointsWeekly());
        Assertions.assertEquals(5, updatedPlayer.getTotalFantasyPoints());
    }

    @Test
    void should_reset_points_for_players_weekly() {
        //given
        List<Player> players = createPlayers();
        players.forEach(p -> p.setFantasyPointsWeekly(FIVE_FANTASY_POINTS));
        when(playerRepository.findAll()).thenReturn(players);

        //when
        playerService.resetWeeklyPoints();

        Assertions.assertEquals(true, players.stream().allMatch(p -> p.getFantasyPointsWeekly().equals(0)));
    }

    @Test
    void should_reset_points_per_GAME() {
        //given
        List<Player> players = createPlayers();
        players.forEach(p -> p.setFantasyPointPerGame(FIVE_FANTASY_POINTS));
        TEAM.setPlayers(players);

        //when
        playerService.resetPointsPerGame(GAME);

        Assertions.assertEquals(true, GAME.getHomeTeam().getPlayers().stream().allMatch(p -> p.getFantasyPointPerGame().equals(0)));
        Assertions.assertEquals(true, GAME.getAwayTeam().getPlayers().stream().allMatch(p -> p.getFantasyPointPerGame().equals(0)));
    }
}