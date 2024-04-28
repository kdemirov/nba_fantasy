package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Jpa repository tests for {@link Game}.
 */
@DataJpaTest(properties = {
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.HSQLDialect"
})
class GameRepositoryTest extends AbstractTestClass {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void find_all_finishedGames() {
        //arrange
        List<Game> finishedGames = gameRepository.findAllByTimeEquals("Final");

        //assert
        Assertions.assertEquals(1, finishedGames.size());
        Assertions.assertEquals("Final", finishedGames.get(0).getTime());
        Assertions.assertEquals(DAY_BEGIN, finishedGames.get(0).getDayBegin());
        Assertions.assertEquals(POINTS_FOR_TEAM, finishedGames.get(0).getPointsHomeTeam());
        Assertions.assertEquals(POINTS_FOR_TEAM, finishedGames.get(0).getPointsAwayTeam());
        Assertions.assertEquals(GAME_DETAILS_URL, finishedGames.get(0).getGameDetailsUrl());
        Assertions.assertEquals(WEEK, finishedGames.get(0).getWeek());
    }

    @Test
    void find_all_unfinished_games() {
        //arrange
        List<Game> finishedGames = gameRepository.findAllByTimeIsNotLike("Final");

        //assert
        Assertions.assertEquals(1, finishedGames.size());
        Assertions.assertEquals(TIME, finishedGames.get(0).getTime());
        Assertions.assertEquals(DAY_BEGIN, finishedGames.get(0).getDayBegin());
        Assertions.assertEquals(0, finishedGames.get(0).getPointsHomeTeam());
        Assertions.assertEquals(0, finishedGames.get(0).getPointsAwayTeam());
        Assertions.assertNull(finishedGames.get(0).getGameDetailsUrl());
        Assertions.assertEquals(WEEK, finishedGames.get(0).getWeek());
    }

    @Test
    void should_throw_exception_when_game_details_url_is_invalid() {
        //arrange
        Game game = gameRepository.findById(1L).orElse(null);
        game.setGameDetailsUrl(Constants.NBA_URL + GAME_DETAILS_URL);

        //assert
        assertThrows(ConstraintViolationException.class, () ->
                testEntityManager.persistAndFlush(game));
        testEntityManager.clear();
    }

    @Test
    void should_save_game_when_game_details_url_is_valid() {
        //arrange
        Game expectedGame = gameRepository.findById(1L).orElse(null);
        expectedGame.setGameDetailsUrl(GAME_DETAILS_URL + "/asdsas");

        //assert
        Game game = testEntityManager.persistAndFlush(expectedGame);
        Assertions.assertEquals(expectedGame.getGameDetailsUrl(), game.getGameDetailsUrl());
    }
}