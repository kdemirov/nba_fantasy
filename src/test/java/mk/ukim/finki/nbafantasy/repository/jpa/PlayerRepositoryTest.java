package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

/**
 * Jpa repository tests for {@link Player}.
 */
@DataJpaTest(properties = {
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.HSQLDialect"
})
class PlayerRepositoryTest extends AbstractTestClass {

    private static final String CENTER_PLAYER_NAME_UPPERCASE = "CENTER_PLAYER";
    private static final String NAME_STARTS_WITH_GUARD = "Guard";
    private static final String INVALID_PLAYER_URL = "playerUrl";
    private static final Integer PLAYERS_SIZE = 4;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void finds_player_by_name_ignoring_case() {
        //arrange
        Optional<Player> player = playerRepository.findByNameLikeIgnoreCase(CENTER_PLAYER_NAME_UPPERCASE);

        //assert
        Assertions.assertEquals(CENTER_PLAYER, player.get().getName());
    }

    @Test
    void finds_players_that_name_starts_with() {
        //arrange
        List<Player> players = playerRepository.findByNameStartsWith(NAME_STARTS_WITH_GUARD);

        //assert
        Assertions.assertEquals(PLAYERS_SIZE, players.size());
    }

    @Test
    void should_throw_exception_if_player_url_is_invalid() {
        //arrange
        Player player = createPlayers().get(0);
        player.setPlayerUrl(INVALID_PLAYER_URL);

        //assert
        Assertions.assertThrows(ConstraintViolationException.class,
                () -> testEntityManager.persistAndFlush(player));
        testEntityManager.clear();
    }
}