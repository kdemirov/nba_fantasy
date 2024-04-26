package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.config.AuthenticationProviderMock;
import mk.ukim.finki.nbafantasy.config.Constants;
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

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * My Team Controller Test.
 */
@WebMvcTest(controllers = {MyTeamController.class})
@Import({DbConfig.class, SecurityConfig.class})
@ActiveProfiles("SECURITY_MOCK")
class MyTeamTest extends AbstractTestClass {
    MockMvc mockMvc;

    @MockBean
    UserService userService;
    @MockBean
    GroupService groupService;

    @MockBean
    GameService gameService;

    @MockBean
    AuthenticationProviderMock authenticationProviderMock;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void should_test_get_my_team_page_without_authentication() throws Exception {
        mockMvc.perform(get(Constants.MY_TEAM_URL).servletPath(Constants.MY_TEAM_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(LOGIN_URL));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_test_get_my_team_page_with_authentication_user_disabled() throws Exception {
        USER.setEnabled(false);
        given(userService.findByUsername(USERNAME))
                .willReturn(USER);
        mockMvc.perform(get(Constants.MY_TEAM_URL).servletPath(Constants.MY_TEAM_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Constants.VERIFY_ACCOUNT_URL));
        verify(userService).findByUsername(USERNAME);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_test_get_my_team_page_user_enabled() throws Exception {
        USER.setEnabled(true);
        USER.setMyTeam(new ArrayList<>());
        given(userService.findByUsername(USERNAME))
                .willReturn(USER);
        mockMvc.perform(get(Constants.MY_TEAM_URL).servletPath(Constants.MY_TEAM_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Constants.TRANSFERS_URL));
        verify(userService, times(2)).findByUsername(USERNAME);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_test_get_my_team_page_user_enabled_with_created_team() throws Exception {
        USER.setEnabled(true);
        USER.setMyTeam(createPlayers());
        given(userService.findByUsername(USERNAME))
                .willReturn(USER);
        mockMvc.perform(get(Constants.MY_TEAM_URL).servletPath(Constants.MY_TEAM_URL))
                .andExpect(status().isOk());
        verify(userService, times(3)).findByUsername(USERNAME);
    }
}