package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.model.Group;
import mk.ukim.finki.nbafantasy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

/**
 * Jpa repository tests for {@link Group}.
 */
@DataJpaTest(properties = {
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.HSQLDialect"
})
class GroupRepositoryTest extends AbstractTestClass {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void should_find_all_groups_by_user() {
        //arrange
        User user = testEntityManager.find(User.class, USERNAME);
        List<Group> groups = groupRepository.findAllByUsersContains(user);

        //assert
        Assertions.assertEquals(1, groups.size());
        Assertions.assertEquals(GROUP_NAME, groups.get(0).getName());
    }
}