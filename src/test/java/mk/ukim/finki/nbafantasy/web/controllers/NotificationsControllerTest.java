package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.config.AuthenticationProviderMock;
import mk.ukim.finki.nbafantasy.config.DbConfig;
import mk.ukim.finki.nbafantasy.config.SecurityConfig;
import mk.ukim.finki.nbafantasy.model.exceptions.UserIsAlreadyInGroupException;
import mk.ukim.finki.nbafantasy.service.GroupService;
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

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * NotificationsController Test
 */
@WebMvcTest(NotificationsController.class)
@Import({DbConfig.class, SecurityConfig.class})
@ActiveProfiles("SECURITY_MOCK")
class NotificationsControllerTest extends AbstractControllerTestClass {

    private static final String NOTIFICATION_ID = "200";

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    AuthenticationProviderMock authenticationProviderMock;

    @MockBean
    AuthenticationSuccessHandler authenticationSuccessHandler;

    @MockBean
    UserService userService;

    @MockBean
    GroupService groupService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_get_notification_page() throws Exception {
        given(userService.fetchNotification(USERNAME))
                .willReturn(List.of());

        mockMvc.perform(get("/notifications"))
                .andExpect(model().attribute("bodyContent", "notifications"))
                .andExpect(model().attribute("notifications", List.of()))
                .andExpect(status().isOk());

        verify(userService).fetchNotification(USERNAME);
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_get_notification_page_with_admin_user() throws Exception {
        mockMvc.perform(get("/notifications"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_join_group_with_exception() throws Exception {
        Exception o_O = new UserIsAlreadyInGroupException(NAME, USERNAME);
        doThrow(o_O).when(groupService).joinGroup(Long.valueOf(GROUP_ID), Long.valueOf(NOTIFICATION_ID), USERNAME);

        mockMvc.perform(post("/notifications/accept/" + GROUP_ID)
                        .param("notificationId", NOTIFICATION_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notifications?error" + o_O.getMessage()));

        verify(groupService).joinGroup(Long.valueOf(GROUP_ID), Long.valueOf(NOTIFICATION_ID), USERNAME);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_join_group() throws Exception {
        mockMvc.perform(post("/notifications/accept/" + GROUP_ID)
                        .param("notificationId", NOTIFICATION_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notifications"));

        verify(groupService).joinGroup(Long.valueOf(GROUP_ID), Long.valueOf(NOTIFICATION_ID), USERNAME);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_decline_invited_group() throws Exception {
        mockMvc.perform(post("/notifications/decline/" + GROUP_ID)
                        .param("notificationId", NOTIFICATION_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notifications"));

        verify(groupService).declineInvitedGroup(Long.valueOf(GROUP_ID), Long.valueOf(NOTIFICATION_ID), USERNAME);
    }
}