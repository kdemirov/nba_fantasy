package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

/**
 * Jpa tests for {@link User}
 */
@DataJpaTest(properties = {
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.HSQLDialect"
})
class UserRepositoryTest extends AbstractTestClass {

    private static final Long FIRST_PLAYER_ID = 1L;
    private static final Long TENTH_PLAYER_ID = 10L;
    private static final Integer USER_SIZE_WITH_PLAYER = 1;
    private static final Integer USER_SIZE_WITH_OUT_PLAYER = 0;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void should_find_by_username() {
        //arrange
        Optional<User> user = userRepository.findByUsername(USERNAME);

        //assert
        Assertions.assertEquals(USER.getUsername(), user.get().getUsername());
    }

    @Test
    void should_not_find_by_username() {
        //arrange
        Optional<User> user = userRepository.findByUsername(TEST_USER.getUsername());

        //assert
        Assertions.assertEquals(Optional.empty(), user);
    }

    @Test
    void should_find_all_users_which_contains_player_in_their_team() {
        //arrange
        Player player = entityManager.find(Player.class, FIRST_PLAYER_ID);
        List<User> users = userRepository.findAllByMyTeamContains(player);

        //assert
        Assertions.assertEquals(USER_SIZE_WITH_PLAYER, users.size());
        Assertions.assertEquals(USERNAME, users.get(0).getUsername());
    }

    @Test
    void should_not_find_all_users_which_contains_player_in_their_team() {
        //arrange
        Player player = entityManager.find(Player.class, TENTH_PLAYER_ID);
        List<User> users = userRepository.findAllByMyTeamContains(player);

        //assert
        Assertions.assertEquals(USER_SIZE_WITH_OUT_PLAYER, users.size());
    }
}