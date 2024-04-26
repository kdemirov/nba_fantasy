package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.config.AuthenticationProviderMock;
import mk.ukim.finki.nbafantasy.config.DbConfig;
import mk.ukim.finki.nbafantasy.config.SecurityConfig;
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

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * VerifyAccount Controller Test
 */
@WebMvcTest(VerifyAccountController.class)
@Import({DbConfig.class, SecurityConfig.class})
@ActiveProfiles("SECURITY_MOCK")
class VerifyAccountTest extends AbstractTestClass {

    MockMvc mockMvc;

    private static final String CONFIRMATION_CODE = UUID.randomUUID().toString();

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    UserService userService;

    @MockBean
    EmailSenderService emailSenderService;

    @MockBean
    AuthenticationProviderMock authenticationProviderMock;

    @MockBean
    AuthenticationSuccessHandler authenticationSuccessHandler;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_get_verify_page() throws Exception {
        mockMvc.perform(get("/verify-account"))
                .andExpect(model().attribute("bodyContent", "verify-account"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_get_verify_page_with_admin_user() throws Exception {
        mockMvc.perform(get("/verify-account"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_verify_account() throws Exception {
        given(userService.findByUsername(USERNAME)).willReturn(USER);
        given(userService.verifyAccount(CONFIRMATION_CODE, USER))
                .willReturn(true);

        mockMvc.perform(post("/verify-account")
                        .param("confirmationCode", CONFIRMATION_CODE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/myteam?message=Succesfully activated account"));

        verify(userService).findByUsername(USERNAME);
        verify(userService).verifyAccount(CONFIRMATION_CODE, USER);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_verify_account_wrong_confirmation_code() throws Exception {
        given(userService.findByUsername(USERNAME)).willReturn(USER);
        given(userService.verifyAccount(CONFIRMATION_CODE, USER))
                .willReturn(false);

        mockMvc.perform(post("/verify-account")
                        .param("confirmationCode", CONFIRMATION_CODE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/verify-account?error=Wrong Code"));

        verify(userService).findByUsername(USERNAME);
        verify(userService).verifyAccount(CONFIRMATION_CODE, USER);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_send_confirmation_code_again() throws Exception {
        given(userService.findByUsername(USERNAME)).willReturn(USER);

        mockMvc.perform(post("/verify-account/send"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/verify-account"));

        verify(userService).findByUsername(USERNAME);
        verify(this.emailSenderService).sendEmail(USER);
    }
}