package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.model.Game;
import mk.ukim.finki.nbafantasy.model.exceptions.GameDoesNotExistException;
import mk.ukim.finki.nbafantasy.repository.jpa.GameRepository;
import mk.ukim.finki.nbafantasy.service.impl.GameServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests for GameService.
 */
class GameServiceTest extends AbstractTestClass {

    @InjectMocks
    GameServiceImpl gameService;

    @Mock
    PlayerService playerService;

    @Mock
    UserService userService;

    @Mock
    TeamService teamService;

    @Mock
    GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_find_game_by_id() {
        //given
        when(gameRepository.findById(any())).thenReturn(Optional.of(GAME));

        //when
        Game game = gameService.findById(any());

        //then
        Assertions.assertEquals(GAME.getWeek(), game.getWeek());
        Assertions.assertEquals(GAME.getHomeTeam().getName(), game.getHomeTeam().getName());
        Assertions.assertEquals(GAME.getAwayTeam().getName(), game.getAwayTeam().getName());
        Assertions.assertEquals(GAME.getTime(), game.getTime());
        Assertions.assertEquals(GAME.getDayBegin(), game.getDayBegin());
    }

    @Test
    void should_not_find_game_by_id() {
        //given
        when(gameRepository.findById(any())).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(GameDoesNotExistException.class,
                () -> gameService.findById(any()));
    }

    @Test
    void should_update_game_with_points_and_game_details_url() {
        //given
        when(gameRepository.findById(any())).thenReturn(Optional.of(GAME));

        //when
        Game game = this.gameService.update(any(), POINTS_FOR_TEAM, POINTS_FOR_TEAM, TIME, GAME_DETAILS_URL);

        //then
        Assertions.assertEquals(POINTS_FOR_TEAM, game.getPointsHomeTeam());
        Assertions.assertEquals(POINTS_FOR_TEAM, game.getPointsAwayTeam());
        Assertions.assertEquals(TIME, game.getTime());
        Assertions.assertEquals(GAME_DETAILS_URL, game.getGameDetailsUrl());
    }

    @Test
    void should_save_game() {
        //given
        when(teamService.findByName(TEAM_NAME)).thenReturn(TEAM);

        //when
        Game game = gameService.saveGame(TEAM_NAME,
                TEAM_NAME,
                DAY_BEGIN,
                TIME,
                WEEK,
                null,
                null,
                null);

        //then
        Assertions.assertEquals(TEAM_NAME, game.getHomeTeam().getName());
        Assertions.assertEquals(TEAM_NAME, game.getAwayTeam().getName());
        Assertions.assertEquals(DAY_BEGIN, game.getDayBegin());
        Assertions.assertEquals(TIME, game.getTime());
        Assertions.assertEquals(WEEK, game.getWeek());
        Assertions.assertEquals(0, game.getPointsHomeTeam());
        Assertions.assertEquals(0, game.getPointsAwayTeam());
        Assertions.assertNull(game.getGameDetailsUrl());
    }

    @Test
    void should_save_game_with_points_and_game_details_url() {
        //given
        when(teamService.findByName(TEAM_NAME)).thenReturn(TEAM);

        //when
        Game game = gameService.saveGame(TEAM_NAME,
                TEAM_NAME,
                DAY_BEGIN,
                TIME,
                WEEK,
                POINTS_FOR_TEAM.toString(),
                POINTS_FOR_TEAM.toString(),
                GAME_DETAILS_URL);

        //then
        Assertions.assertEquals(TEAM_NAME, game.getHomeTeam().getName());
        Assertions.assertEquals(TEAM_NAME, game.getAwayTeam().getName());
        Assertions.assertEquals(DAY_BEGIN, game.getDayBegin());
        Assertions.assertEquals(TIME, game.getTime());
        Assertions.assertEquals(WEEK, game.getWeek());
        Assertions.assertEquals(POINTS_FOR_TEAM, game.getPointsHomeTeam());
        Assertions.assertEquals(POINTS_FOR_TEAM, game.getPointsAwayTeam());
        Assertions.assertEquals(GAME_DETAILS_URL, game.getGameDetailsUrl());
    }

    @Test
    void should_find_all_finished_games_by_week() {
        //given
        TreeMap<String, List<Game>> exceptedGamesByWeek = new TreeMap<>();
        GAME.setTime(Constants.GAME_FINISHED);
        exceptedGamesByWeek.putIfAbsent(WEEK, List.of(GAME));
        when(gameRepository.findAllByTimeEquals(any())).thenReturn(List.of(GAME));

        //when
        Map<String, List<Game>> gamesByWeek = gameService.findAllFinishedGames();

        //then
        Assertions.assertEquals(true, gamesByWeek.containsKey(WEEK));
        Assertions.assertEquals(1, gamesByWeek.get(WEEK).size());
        Assertions.assertEquals(exceptedGamesByWeek, gamesByWeek);
    }

    @Test
    void should_find_all_unfinished_games_by_week() {
        //given
        TreeMap<String, List<Game>> exceptedGamesByWeek = new TreeMap<>();
        GAME.setTime(TIME);
        exceptedGamesByWeek.putIfAbsent(WEEK, List.of(GAME));
        when(gameRepository.findAllByTimeEquals(any())).thenReturn(List.of(GAME));

        //when
        Map<String, List<Game>> gamesByWeek = gameService.findAllFinishedGames();

        //then
        Assertions.assertEquals(true, gamesByWeek.containsKey(WEEK));
        Assertions.assertEquals(1, gamesByWeek.get(WEEK).size());
        Assertions.assertEquals(exceptedGamesByWeek, gamesByWeek);
    }
}