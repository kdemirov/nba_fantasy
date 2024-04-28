package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.model.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

/**
 * Jpa Tests for {@link Team}
 */
@DataJpaTest(properties = {
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.HSQLDialect"
})
class TeamRepositoryTest extends AbstractTestClass {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void find_by_name_ignore_case() {
        //arrange
        String expectedName = "teamName";
        Optional<Team> team = teamRepository.findByNameIgnoreCase(TEAM_NAME);

        //assert
        Assertions.assertEquals(expectedName, team.get().getName());
    }

    @Test
    void should_find_all_teams_pageable() {
        //arrange
        List<Team> teams = teamRepository.findAll(PageRequest.of(0, 1)).toList();

        //assert
        Assertions.assertEquals(1, teams.size());
    }

    @Test
    void should_throw_exception_if_url_is_invalid() {
        //assert
        Assertions.assertThrows(ConstraintViolationException.class,
                () -> testEntityManager.persistAndFlush(new Team(DIVISION,
                        TEAM_NAME,
                        null,
                        Constants.NBA_URL + PLAYERS_URL,
                        IMAGE_URL)));
        testEntityManager.clear();
    }
}