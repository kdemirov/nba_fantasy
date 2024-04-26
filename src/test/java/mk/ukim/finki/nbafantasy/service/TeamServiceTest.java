package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.model.Team;
import mk.ukim.finki.nbafantasy.model.exceptions.TeamDoesNotExistException;
import mk.ukim.finki.nbafantasy.repository.jpa.TeamRepository;
import mk.ukim.finki.nbafantasy.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * TeamService Tests.
 */
class TeamServiceTest extends AbstractTestClass {

    private static final Integer TEAMS_SIZE = 1;
    @InjectMocks
    TeamServiceImpl teamService;

    @Mock
    TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_find_all_teams() {
        //given
        when(teamRepository.findAll()).thenReturn(List.of(TEAM));

        //when
        List<Team> teams = teamService.getAll();

        //then
        Assertions.assertEquals(TEAMS_SIZE, teams.size());
        Assertions.assertEquals(TEAM.getName(), teams.get(0).getName());
        Assertions.assertEquals(TEAM.getConference(), teams.get(0).getConference());
    }

    @Test
    void should_save_team() {
        //when
        Team team = teamService.saveTeam(DIVISION, TEAM_NAME, null, PLAYERS_URL, IMAGE_URL);

        //then
        Assertions.assertEquals(TEAM_NAME, team.getName());
        Assertions.assertEquals(DIVISION, team.getConference());
        Assertions.assertNull(team.getCode());
        Assertions.assertEquals(PLAYERS_URL, team.getPlayersUrl());
        Assertions.assertEquals(IMAGE_URL, team.getImageUrl());
    }

    @Test
    void should_find_team_by_id() {
        //given
        when(teamRepository.findById(ID)).thenReturn(Optional.of(TEAM));

        //when
        Team team = teamService.findById(ID);

        //then
        Assertions.assertEquals(TEAM.getName(), team.getName());
        Assertions.assertEquals(TEAM.getConference(), team.getConference());
        Assertions.assertEquals(TEAM.getImageUrl(), team.getImageUrl());
        Assertions.assertEquals(TEAM.getPlayersUrl(), team.getPlayersUrl());
    }

    @Test
    void should_not_find_team_by_id() {
        //given
        when(teamRepository.findById(ID)).thenReturn(Optional.of(TEAM));

        //when
        Team team = teamService.findById(ID);

        //then
        Assertions.assertEquals(TEAM.getName(), team.getName());
        Assertions.assertEquals(TEAM.getConference(), team.getConference());
        Assertions.assertEquals(TEAM.getImageUrl(), team.getImageUrl());
        Assertions.assertEquals(TEAM.getPlayersUrl(), team.getPlayersUrl());
    }

    @Test
    void should_throw_exception_when_team_by_id_does_not_exists() {
        //given
        when(teamRepository.findById(ID)).thenReturn(Optional.empty());

        //when
        assertThrows(TeamDoesNotExistException.class,
                () -> teamService.findById(ID));
    }

    @Test
    void should_find_team_by_name() {
        //given
        when(teamRepository.findByNameIgnoreCase(TEAM_NAME)).thenReturn(Optional.of(TEAM));

        //when
        Team team = teamService.findByName(TEAM_NAME);

        //then
        Assertions.assertEquals(TEAM_NAME, team.getName());
        Assertions.assertEquals(DIVISION, team.getConference());
        Assertions.assertEquals(IMAGE_URL, team.getImageUrl());
        Assertions.assertEquals(PLAYERS_URL, team.getPlayersUrl());
    }

    @Test
    void should_throw_exception_when_team_by_name_does_not_exist() {
        //given
        when(teamRepository.findByNameIgnoreCase(TEAM_NAME)).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(TeamDoesNotExistException.class,
                () -> teamService.findByName(TEAM_NAME));
    }

    @Test
    void should_update_team_players() {
        //given
        TEAM.setId(ID);
        when(teamRepository.findById(ID)).thenReturn(Optional.of(TEAM));
        TEAM.setPlayers(createPlayers());
        when(teamRepository.save(TEAM)).thenReturn(TEAM);
        //when
        Team team = teamService.update(TEAM);

        //then
        Assertions.assertEquals(5, team.getPlayers().size());
    }
}