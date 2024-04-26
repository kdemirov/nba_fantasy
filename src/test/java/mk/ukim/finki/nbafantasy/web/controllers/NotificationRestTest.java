package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.config.AuthenticationProviderMock;
import mk.ukim.finki.nbafantasy.config.DbConfig;
import mk.ukim.finki.nbafantasy.model.dto.GroupDto;
import mk.ukim.finki.nbafantasy.model.dto.NotificationDto;
import mk.ukim.finki.nbafantasy.model.dto.PlayerDto;
import mk.ukim.finki.nbafantasy.model.dto.UserDto;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.apache.catalina.security.SecurityConfig;
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
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * NotificationRestController Test.
 */
@WebMvcTest(NotificationRestController.class)
@Import({DbConfig.class, SecurityConfig.class})
@ActiveProfiles("SECURITY_MOCK")
class NotificationRestTest extends AbstractTestClass {

    private static final PlayerDto PLAYER_DTO = PlayerDto.builder()
            .name(NAME)
            .fantasyPointsPerGame(2)
            .id(2L)
            .playerImageUrl(IMAGE_URL)
            .build();
    private static final NotificationDto NOTIFICATION_DTO = NotificationDto
            .builder()
            .group(GroupDto
                    .builder()
                    .users(List.of(
                            UserDto.builder()
                                    .guardPlayers(List.of(PLAYER_DTO))
                                    .centerPlayer(PLAYER_DTO)
                                    .forwardPlayers(List.of(PLAYER_DTO))
                                    .build()
                    ))
                    .groupPoints(12.0)
                    .name(NAME)
                    .id(1L)
                    .build())
            .build();

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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_fetch_notification() throws Exception {
        given(userService.fetchNotification(USERNAME)).willReturn(List.of(NOTIFICATION_DTO));

        mockMvc.perform(post("/users/notification"))
                .andExpect(status().isOk());

        verify(userService).fetchNotification(USERNAME);
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_fetch_notification_with_admin_user() throws Exception {
        mockMvc.perform(post("/users/notification"))
                .andExpect(status().isForbidden());
    }
}