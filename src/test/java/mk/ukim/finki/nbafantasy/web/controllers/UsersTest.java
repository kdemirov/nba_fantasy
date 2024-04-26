package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.config.AuthenticationProviderMock;
import mk.ukim.finki.nbafantasy.config.DbConfig;
import mk.ukim.finki.nbafantasy.config.SecurityConfig;
import mk.ukim.finki.nbafantasy.service.GameService;
import mk.ukim.finki.nbafantasy.service.GroupService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserController class
 */
@WebMvcTest(UsersController.class)
@Import(value = {DbConfig.class, SecurityConfig.class})
@ActiveProfiles("SECURITY_MOCK")
class UsersTest extends AbstractTestClass {

    private static final String VIEW_TEST_USER_TEAM_URL = "/users/test";
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;
    @MockBean
    UserService userService;
    @MockBean
    GroupService groupService;
    @MockBean
    GameService gameService;
    @MockBean
    AuthenticationProviderMock authenticationProviderMock;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void should_test_get_another_users_teams_page_without_authentication() throws Exception {
        mockMvc.perform(get(VIEW_TEST_USER_TEAM_URL).servletPath(VIEW_TEST_USER_TEAM_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LOGIN_URL));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_test_get_another_users_teams_page_with_authentication_without_selected_team() throws Exception {
        given(userService.findByUsername(USERNAME)).willReturn(USER);
        given(userService.findByUsername(USERNAME_TEST)).willReturn(TEST_USER);
        mockMvc.perform(get(VIEW_TEST_USER_TEAM_URL).servletPath(VIEW_TEST_USER_TEAM_URL))
                .andExpect(status().isOk());
        verify(userService).findByUsername(USERNAME);
        verify(userService).findByUsername(USERNAME_TEST);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_test_get_another_users_teams_page_with_authentication_with_selected_team() throws Exception {
        given(userService.findByUsername(USERNAME)).willReturn(USER);
        TEST_USER.setMyTeam(createPlayers());
        given(userService.findByUsername(USERNAME_TEST)).willReturn(TEST_USER);
        mockMvc.perform(get(VIEW_TEST_USER_TEAM_URL).servletPath(VIEW_TEST_USER_TEAM_URL))
                .andExpect(status().isOk());
        verify(userService).findByUsername(USERNAME);
        verify(userService).findByUsername(USERNAME_TEST);
    }
}