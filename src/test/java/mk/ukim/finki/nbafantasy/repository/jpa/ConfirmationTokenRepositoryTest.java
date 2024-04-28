package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.model.ConfirmationToken;
import mk.ukim.finki.nbafantasy.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

/**
 * Jpa tests for {@link ConfirmationToken}.
 */
@DataJpaTest(properties = {
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.HSQLDialect"
})
class ConfirmationTokenRepositoryTest extends AbstractTestClass {

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void find_confirmation_token_by_user() {
        //arrange
        User user = testEntityManager.find(User.class, USERNAME);
        ConfirmationToken expectedConfirmationToken = new ConfirmationToken(user);
        expectedConfirmationToken = testEntityManager.persistAndFlush(expectedConfirmationToken);
        testEntityManager.clear();

        //assert
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByUser(user);
        Assert.assertEquals(expectedConfirmationToken.getConfirmationToken(), confirmationToken.get().getConfirmationToken());
    }
}