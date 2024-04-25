package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.config.AuthenticationProviderMock;
import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.config.DbConfig;
import mk.ukim.finki.nbafantasy.config.SecurityConfig;
import mk.ukim.finki.nbafantasy.model.exceptions.UsernameAlreadyExistException;
import mk.ukim.finki.nbafantasy.service.UserService;
import mk.ukim.finki.nbafantasy.service.impl.EmailSenderService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * RegisterController Tests.
 */
@WebMvcTest(RegisterController.class)
@Import({DbConfig.class, SecurityConfig.class})
@ActiveProfiles("SECURITY_MOCK")
class RegisterControllerTest extends AbstractControllerTestClass {

    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    EmailSenderService emailSenderService;

    @MockBean
    AuthenticationProviderMock authenticationProviderMock;

    @MockBean
    AuthenticationSuccessHandler authenticationSuccessHandler;

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
    void should_test_get_register_page() throws Exception {
        mockMvc.perform(get(Constants.REGISTER_URL).servletPath(Constants.REGISTER_URL))
                .andExpect(status().isOk());
    }

    @Test
    void should_register_user() throws Exception {
        mockMvc.perform(post(Constants.REGISTER_URL).servletPath(Constants.REGISTER_URL)
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .param("email", EMAIL)
                        .param("name", NAME)
                        .param("surname", SURNAME))
                .andExpect(status().isOk());
    }

    @Test
    void should_register_user_with_username_exists() throws Exception {
        Exception o_O = new UsernameAlreadyExistException(String.format("Username: %s already exists", USERNAME));
        given(userService.register(USERNAME, PASSWORD, EMAIL, NAME, SURNAME, ROLE))
                .willThrow(o_O);

        mockMvc.perform(post(Constants.REGISTER_URL).servletPath(Constants.REGISTER_URL)
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .param("email", EMAIL)
                        .param("name", NAME)
                        .param("surname", SURNAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Constants.REGISTER_URL + "?error=" + o_O.getMessage()));

        verify(userService).register(USERNAME, PASSWORD, EMAIL, NAME, SURNAME, ROLE);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_redirect_user_if_is_authenticated_and_navigates_to_register_url() throws Exception {
        given(userService.findByUsername(USERNAME))
                .willReturn(USER);
        USER.setMyTeam(new ArrayList<>());
        mockMvc.perform(get(Constants.REGISTER_URL).servletPath(Constants.REGISTER_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Constants.TRANSFERS_URL));

        verify(userService).findByUsername(USERNAME);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_redirect_user_if_is_authenticated_and_navigates_to_register_url_with_selected_team() throws Exception {
        given(userService.findByUsername(USERNAME))
                .willReturn(USER);
        USER.setMyTeam(createPlayers());

        mockMvc.perform(get(Constants.REGISTER_URL).servletPath(Constants.REGISTER_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Constants.MY_TEAM_URL));

        verify(userService).findByUsername(USERNAME);
    }
}