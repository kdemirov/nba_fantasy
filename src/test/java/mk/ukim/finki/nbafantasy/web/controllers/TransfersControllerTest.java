package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.config.AuthenticationProviderMock;
import mk.ukim.finki.nbafantasy.config.DbConfig;
import mk.ukim.finki.nbafantasy.config.SecurityConfig;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.exceptions.CenterPlayerAlreadyExistException;
import mk.ukim.finki.nbafantasy.service.GameService;
import mk.ukim.finki.nbafantasy.service.TeamService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TransfersController Test.
 */
@WebMvcTest(TransfersController.class)
@Import({DbConfig.class, SecurityConfig.class})
@ActiveProfiles("SECURITY_MOCK")
class TransfersControllerTest extends AbstractControllerTestClass {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;
    @MockBean
    AuthenticationProviderMock authenticationProviderMock;
    @MockBean
    AuthenticationSuccessHandler authenticationSuccessHandler;

    @MockBean
    TeamService teamService;

    @MockBean
    UserService userService;

    @MockBean
    GameService gameService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_get_transfers_page() throws Exception {
        List<Player> players = createPlayers();
        players.forEach(p -> p.setTeam(TEAM));
        USER.setMyTeam(players);
        given(userService.findByUsername(USERNAME))
                .willReturn(USER);

        given(teamService.paginationTeams()).willReturn(List.of(List.of(TEAM)));
        given(gameService.findAllUnfinishedGames()).willReturn(gamesByWeek);

        mockMvc.perform(get("/transfers"))
                .andExpect(model().attribute("bodyContent", "transfers"))
                .andExpect(model().attribute("teams", List.of(TEAM)))
                .andExpect(model().attribute("myteam", USER.getMyTeam()))
                .andExpect(model().attribute("notifications", USER.getNotifications()))
                .andExpect(model().attribute("games", gamesByWeek))
                .andExpect(status().isOk());

        verify(userService).findByUsername(USERNAME);
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_get_transfers_page_with_admin_user() throws Exception {
        mockMvc.perform(get("/transfers"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_get_pagination_pages_for_all_teams() throws Exception {
        USER.setMyTeam(new ArrayList<>());
        given(userService.findByUsername(USERNAME))
                .willReturn(USER);
        given(teamService.paginationTeams()).willReturn(
                List.of(List.of(TEAM))
        );
        given(gameService.findAllUnfinishedGames())
                .willReturn(gamesByWeek);

        mockMvc.perform(get("/transfers/0"))
                .andExpect(model().attribute("bodyContent", "transfers"))
                .andExpect(model().attribute("teams", List.of(TEAM)))
                .andExpect(model().attribute("myteam", USER.getMyTeam()))
                .andExpect(model().attribute("notifications", USER.getNotifications()))
                .andExpect(model().attribute("games", gamesByWeek))
                .andExpect(status().isOk());

        verify(userService).findByUsername(USERNAME);
        verify(teamService).paginationTeams();
        verify(gameService).findAllUnfinishedGames();
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_make_transfer_with_exception() throws Exception {
        Exception o_O = new CenterPlayerAlreadyExistException(NAME);
        given(userService.addPlayer(USERNAME, Long.valueOf(PLAYER_ID)))
                .willThrow(o_O);

        mockMvc.perform(post("/transfers")
                        .param("playerId", PLAYER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfers?error=" + o_O.getMessage()));

        verify(userService).addPlayer(USERNAME, Long.valueOf(PLAYER_ID));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_make_transfer() throws Exception {
        given(userService.addPlayer(USERNAME, Long.valueOf(PLAYER_ID)))
                .willReturn(createPlayers().get(0));

        mockMvc.perform(post("/transfers")
                        .param("playerId", PLAYER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfers"));

        verify(userService).addPlayer(USERNAME, Long.valueOf(PLAYER_ID));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_remove_player_from_user_team() throws Exception {
        mockMvc.perform(post("/transfers/delete/" + PLAYER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfers"));

        verify(userService).deletePlayer(USERNAME, Long.valueOf(PLAYER_ID));
    }
}