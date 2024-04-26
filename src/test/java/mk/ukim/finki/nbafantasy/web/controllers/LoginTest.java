package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.config.AuthenticationProviderMock;
import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.config.DbConfig;
import mk.ukim.finki.nbafantasy.config.SecurityConfig;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * LoginController Test
 */
@WebMvcTest(LoginController.class)
@Import({DbConfig.class, SecurityConfig.class})
@ActiveProfiles("SECURITY_MOCK")
class LoginTest extends AbstractTestClass {
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    UserService userService;

    @MockBean
    AuthenticationProviderMock authenticationProviderMock;

    @MockBean
    AuthenticationSuccessHandler authenticationSuccessHandler;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void getLoginPage() throws Exception {
        mockMvc.perform(get(Constants.LOGIN_URL).servletPath(Constants.LOGIN_URL))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_redirect_to_my_team_page_if_user_is_authenticated_and_has_selected_team() throws Exception {
        USER.setMyTeam(createPlayers());
        given(userService.findByUsername(USERNAME))
                .willReturn(USER);

        mockMvc.perform(get(Constants.LOGIN_URL).servletPath(Constants.LOGIN_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Constants.MY_TEAM_URL));

        verify(userService).findByUsername(USERNAME);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_redirect_to_my_team_page_if_user_is_authenticated_without_selected_team() throws Exception {
        USER.setMyTeam(new ArrayList<>());
        given(userService.findByUsername(USERNAME))
                .willReturn(USER);

        mockMvc.perform(get(Constants.LOGIN_URL).servletPath(Constants.LOGIN_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Constants.TRANSFERS_URL));

        verify(userService).findByUsername(USERNAME);
    }
}