package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.config.AuthenticationProviderMock;
import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.config.DbConfig;
import mk.ukim.finki.nbafantasy.config.SecurityConfig;
import mk.ukim.finki.nbafantasy.data_retrieval.model.ParsedDocument;
import mk.ukim.finki.nbafantasy.data_retrieval.service.RetrievalDataService;
import mk.ukim.finki.nbafantasy.model.Game;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.service.GameService;
import mk.ukim.finki.nbafantasy.service.PlayerService;
import mk.ukim.finki.nbafantasy.service.TeamService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.jsoup.nodes.Element;
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
 * AdminController Test
 */
@WebMvcTest(AdminController.class)
@Import({DbConfig.class, SecurityConfig.class})
@ActiveProfiles("SECURITY_MOCK")
class AdminTest extends AbstractTestClass {

    MockMvc mockMvc;

    private static final String TEAM_ID = "200";
    private static final String CLASS_NAME = ".cssClass";
    private static final String HOME_TEAM = "HOMETEAM";
    private static final String AWAY_TEAM = "AWAYTEAM";

    private static final ParsedDocument PARSED_DOCUMENT = ParsedDocument.builder()
            .parsedBody(new Element(Constants.DIV_ELEMENT))
            .cssNodes(new ArrayList<>())
            .build();

    @Autowired
    WebApplicationContext webApplicationContext;
    @MockBean
    TeamService teamService;
    @MockBean
    PlayerService playerService;
    @MockBean
    GameService gameService;
    @MockBean
    UserService userService;
    @MockBean
    RetrievalDataService extractDataService;
    @MockBean
    AuthenticationProviderMock authenticationProviderMock;
    @MockBean
    AuthenticationSuccessHandler authenticationSuccessHandler;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_get_admin_panel_home_page_role_user() throws Exception {
        mockMvc.perform(get("/admin/panel"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_get_admin_panel_home_page() throws Exception {
        mockMvc.perform(get("/admin/panel"))
                .andExpect(model().attributeExists("bodyContent"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_save_team() throws Exception {
        given(teamService.saveTeam(DIVISION, TEAM_NAME, "", PLAYERS_URL, IMAGE_URL))
                .willReturn(TEAM);

        mockMvc.perform(post("/admin/panel/saveTeam")
                        .param("teamName", TEAM_NAME)
                        .param("imageUrl", IMAGE_URL)
                        .param("division", DIVISION)
                        .param("playersUrl", PLAYERS_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/panel/teams"));

        verify(teamService).saveTeam(DIVISION, TEAM_NAME, "", PLAYERS_URL, IMAGE_URL);
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_save_players() throws Exception {
        mockMvc.perform(post("/admin/panel/savePlayers")
                        .param("teamId", TEAM_ID)
                        .param("className", CLASS_NAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/panel/players"));

        verify(playerService).getPlayers(Long.valueOf(TEAM_ID), CLASS_NAME);
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_extract_data_for_saving_teams() throws Exception {
        given(extractDataService.retrieveDataFromUrl(Constants.TEAMS_URL, false))
                .willReturn(PARSED_DOCUMENT);

        mockMvc.perform(post("/admin/panel/getTeams"))
                .andExpect(model().attributeExists("bodyContent"))
                .andExpect(model().attributeExists("cssLinks"))
                .andExpect(model().attributeExists("selectableData"))
                .andExpect(model().attributeExists("modalBody"))
                .andExpect(model().attribute("modalBody", "save-team-modal-body"))
                .andExpect(status().isOk());

        verify(extractDataService).retrieveDataFromUrl(Constants.TEAMS_URL, false);
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_get_teams_page() throws Exception {
        given(teamService.getAll()).willReturn(List.of(TEAM));

        mockMvc.perform(get("/admin/panel/teams"))
                .andExpect(model().attribute("bodyContent", "admin-panel-teams"))
                .andExpect(model().attribute("teams", List.of(TEAM)))
                .andExpect(status().isOk());

        verify(teamService).getAll();
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_delete_team_with_given_id() throws Exception {

        mockMvc.perform(post("/admin/panel/teams/delete/" + TEAM_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/panel/teams"));

        verify(teamService).deleteTeam(Long.valueOf(TEAM_ID));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_fetch_the_players_for_team_with_given_id() throws Exception {
        given(teamService.findById(Long.valueOf(TEAM_ID))).willReturn(TEAM);
        given(extractDataService.retrieveDataFromUrl(Constants.NBA_URL + TEAM.getPlayersUrl(), false))
                .willReturn(PARSED_DOCUMENT);

        mockMvc.perform(post("/admin/panel/getPlayers/" + TEAM_ID))
                .andExpect(model().attribute("bodyContent", "admin-panel-teams"))
                .andExpect(model().attribute("cssLinks", PARSED_DOCUMENT.getCssNodes()))
                .andExpect(model().attribute("selectableData", PARSED_DOCUMENT.getParsedBody().outerHtml()))
                .andExpect(model().attribute("modalBody", "save-players-modal-body"))
                .andExpect(status().isOk());

        verify(teamService).findById(Long.valueOf(TEAM_ID));
        verify(extractDataService).retrieveDataFromUrl(Constants.NBA_URL + TEAM.getPlayersUrl(), false);
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_get_players_page() throws Exception {
        List<Player> players = createPlayers();
        players.forEach(p -> p.setTeam(TEAM));
        given(playerService.findAll()).willReturn(players);

        mockMvc.perform(get("/admin/panel/players"))
                .andExpect(model().attribute("bodyContent", "admin-panel-players"))
                .andExpect(model().attribute("players", players))
                .andExpect(status().isOk());

        verify(playerService).findAll();
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_get_player_edit_page_with_given_id() throws Exception {
        Player player = createPlayers().get(0);
        player.setTeam(TEAM);
        given(playerService.findById(Long.valueOf(PLAYER_ID)))
                .willReturn(player);

        mockMvc.perform(get("/admin/panel/players/edit-page/" + PLAYER_ID))
                .andExpect(model().attribute("bodyContent", "admin-panel-edit-player"))
                .andExpect(model().attribute("player", player))
                .andExpect(status().isOk());

        verify(playerService).findById(Long.valueOf(PLAYER_ID));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_fill_players_image_url() throws Exception {

        mockMvc.perform(post("/admin/panel/players/fillPlayersImageUrl")
                        .param("className", CLASS_NAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/panel/players"));

        verify(playerService).fillPlayersImageUrl(CLASS_NAME);
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_get_games_page() throws Exception {

        given(gameService.findAllFinishedGames()).willReturn(gamesByWeek);
        given(gameService.findAllUnfinishedGames()).willReturn(gamesByWeek);

        mockMvc.perform(get("/admin/panel/games"))
                .andExpect(model().attribute("games", gamesByWeek))
                .andExpect(model().attribute("finishedGames", gamesByWeek))
                .andExpect(model().attribute("bodyContent", "admin-panel-games"))
                .andExpect(status().isOk());

        verify(gameService).findAllFinishedGames();
        verify(gameService).findAllUnfinishedGames();
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_fetch_parsed_document_for_saving_games() throws Exception {
        given(extractDataService.retrieveDataFromUrl(Constants.GAMES_URL, true))
                .willReturn(PARSED_DOCUMENT);
        given(gameService.findAllUnfinishedGames()).willReturn(gamesByWeek);
        given(gameService.findAllFinishedGames()).willReturn(gamesByWeek);

        mockMvc.perform(get("/admin/panel/addGame"))
                .andExpect(model().attribute("selectableData", PARSED_DOCUMENT.getParsedBody()))
                .andExpect(model().attribute("cssLinks", PARSED_DOCUMENT.getCssNodes()))
                .andExpect(model().attribute("games", gamesByWeek))
                .andExpect(model().attribute("finishedGames", gamesByWeek))
                .andExpect(model().attribute("bodyContent", "admin-panel-games"))
                .andExpect(model().attribute("modalBody", "save-games-modal-body"));

        verify(extractDataService).retrieveDataFromUrl(Constants.GAMES_URL, true);
        verify(gameService).findAllUnfinishedGames();
        verify(gameService).findAllFinishedGames();
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_save_game_from_selected_data() throws Exception {
        mockMvc.perform(post("/admin/panel/addGame")
                        .param("homeTeam", HOME_TEAM)
                        .param("awayTeam", AWAY_TEAM)
                        .param("dayBegin", DAY_BEGIN)
                        .param("time", TIME)
                        .param("week", WEEK))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/panel/games"));

        verify(this.gameService).saveGame(HOME_TEAM,
                AWAY_TEAM,
                DAY_BEGIN,
                TIME, WEEK,
                null,
                null,
                null);
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_save_game_from_selected_data_with_all_params() throws Exception {
        mockMvc.perform(post("/admin/panel/addGame")
                        .param("homeTeam", HOME_TEAM)
                        .param("awayTeam", AWAY_TEAM)
                        .param("dayBegin", DAY_BEGIN)
                        .param("time", TIME)
                        .param("week", WEEK)
                        .param("pointsHomeTeam", POINTS_FOR_TEAM.toString())
                        .param("pointsAwayTeam", POINTS_FOR_TEAM.toString())
                        .param("gameDetailsUrl", GAME_DETAILS_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/panel/games"));

        verify(this.gameService).saveGame(HOME_TEAM,
                AWAY_TEAM,
                DAY_BEGIN,
                TIME,
                WEEK,
                POINTS_FOR_TEAM.toString(),
                POINTS_FOR_TEAM.toString(),
                GAME_DETAILS_URL);
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_get_edit_game_page() throws Exception {
        Game game = new Game();
        game.setHomeTeam(TEAM);
        game.setAwayTeam(TEAM);
        given(gameService.findById(Long.valueOf(GAME_ID))).willReturn(game);

        mockMvc.perform(get("/admin/panel/games/edit/" + GAME_ID))
                .andExpect(model().attribute("bodyContent", "admin-panel-edit-game"))
                .andExpect(model().attribute("game", game))
                .andExpect(status().isOk());

        verify(gameService).findById(Long.valueOf(GAME_ID));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_save_edited_player() throws Exception {
        mockMvc.perform(post("/admin/panel/players/edit/" + PLAYER_ID)
                        .param("name", NAME)
                        .param("number", PLAYER_NUMBER.toString())
                        .param("height", HEIGHT)
                        .param("weightInLbs", WEIGHT_LBS)
                        .param("birthDate", BIRTH_DATE)
                        .param("age", AGE.toString())
                        .param("experience", EXPERIENCE)
                        .param("school", SCHOOL)
                        .param("price", "4.20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/panel/players"));

        verify(playerService).update(
                Long.valueOf(PLAYER_ID),
                NAME,
                PLAYER_NUMBER,
                HEIGHT,
                WEIGHT_LBS,
                BIRTH_DATE,
                AGE,
                EXPERIENCE,
                SCHOOL,
                4.20);
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_save_edited_game() throws Exception {
        mockMvc.perform(post("/admin/panel/games/edit/" + GAME_ID)
                        .param("pointsHomeTeam", POINTS_FOR_TEAM.toString())
                        .param("pointsAwayTeam", POINTS_FOR_TEAM.toString())
                        .param("time", TIME)
                        .param("gameDetailsUrl", GAME_DETAILS_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/panel/games"));

        verify(this.gameService).update(
                Long.valueOf(GAME_ID),
                POINTS_FOR_TEAM,
                POINTS_FOR_TEAM,
                TIME,
                GAME_DETAILS_URL
        );
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_calculate_fantasy_points_per_game_from_provided_game_details_url() throws Exception {
        mockMvc.perform(post("/admin/panel/games/details/" + GAME_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/panel/getDetailsForGame/" + GAME_ID));

        verify(gameService).getGameDetails(Long.valueOf(GAME_ID));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_get_game_details_page_with_calculated_points() throws Exception {
        Game game = new Game();
        game.setHomeTeam(TEAM);
        game.setAwayTeam(TEAM);
        given(gameService.findById(Long.valueOf(GAME_ID))).willReturn(game);

        mockMvc.perform(get("/admin/panel/getDetailsForGame/" + GAME_ID))
                .andExpect(model().attribute("bodyContent", "admin-panel-gamedetails"))
                .andExpect(model().attribute("game", game))
                .andExpect(status().isOk());

        verify(gameService).findById(Long.valueOf(GAME_ID));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_reset_players_and_users_weekly_points() throws Exception {
        mockMvc.perform(post("/admin/panel/resetWeeklyPoints"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/panel"));

        verify(userService).resetWeeklyPoints();
        verify(playerService).resetWeeklyPoints();
    }
}