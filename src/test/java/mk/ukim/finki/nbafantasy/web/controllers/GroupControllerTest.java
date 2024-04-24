package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.config.AuthenticationProviderMock;
import mk.ukim.finki.nbafantasy.config.DbConfig;
import mk.ukim.finki.nbafantasy.config.SecurityConfig;
import mk.ukim.finki.nbafantasy.service.GroupService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * GroupController Tests.
 */
@WebMvcTest(GroupController.class)
@ActiveProfiles("SECURITY_MOCK")
@Import({DbConfig.class, SecurityConfig.class})
class GroupControllerTest extends AbstractControllerTestClass {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    AuthenticationProviderMock authenticationProviderMock;

    @MockBean
    AuthenticationSuccessHandler authenticationSuccessHandler;

    @MockBean
    GroupService groupService;

    @MockBean
    UserService userService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_get_group_page() throws Exception {
        given(userService.findByUsername(USERNAME))
                .willReturn(USER);

        mockMvc.perform(get("/groups"))
                .andExpect(model().attribute("bodyContent", "groups"))
                .andExpect(model().attribute("myGroups", USER.getGroups()))
                .andExpect(model().attribute("notifications", USER.getNotifications()))
                .andExpect(status().isOk());

        verify(userService).findByUsername(USERNAME);
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, password = PASSWORD, roles = {ADMIN_ROLE})
    void should_get_group_page_with_admin_user() throws Exception {
        mockMvc.perform(get("/groups"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_get_edit_group_page() throws Exception {
        GROUP.setId(Long.valueOf(GROUP_ID));
        given(groupService.findById(Long.valueOf(GROUP_ID)))
                .willReturn(GROUP);

        mockMvc.perform(get("/groups/edit/" + GROUP_ID))
                .andExpect(model().attribute("bodyContent", "edit-group"))
                .andExpect(model().attribute("group", GROUP))
                .andExpect(status().isOk());

        verify(groupService).findById(Long.valueOf(GROUP_ID));
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_return_add_group_page() throws Exception {
        mockMvc.perform(get("/groups/add"))
                .andExpect(model().attribute("bodyContent", "add-group"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_save_group() throws Exception {
        mockMvc.perform(post("/groups/add")
                        .param("name", NAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));

        verify(groupService).save(NAME, USERNAME);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_update_group_with_given_id() throws Exception {
        mockMvc.perform(post("/groups/edit/" + GROUP_ID)
                        .param("name", NAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));

        verify(groupService).update(Long.valueOf(GROUP_ID), NAME);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_search_for_user_for_inviting_in_a_group_with_given_id() throws Exception {
        given(groupService.findById(Long.valueOf(GROUP_ID)))
                .willReturn(GROUP);
        given(userService.findByUsername(USERNAME_TEST))
                .willReturn(TEST_USER);

        mockMvc.perform(post("/groups/edit/search")
                        .param("groupId", GROUP_ID)
                        .param("search", USERNAME_TEST))
                .andExpect(model().attribute("bodyContent", "edit-group"))
                .andExpect(model().attribute("group", GROUP))
                .andExpect(model().attribute("searchedUser", TEST_USER))
                .andExpect(status().isOk());

        verify(groupService).findById(Long.valueOf(GROUP_ID));
        verify(userService).findByUsername(USERNAME_TEST);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_search_for_user_for_inviting_in_a_group_with_given_id_and_not_found_the_user() throws Exception {
        given(groupService.findById(Long.valueOf(GROUP_ID)))
                .willReturn(GROUP);
        Exception o_O = new UsernameNotFoundException(USERNAME_TEST);
        given(userService.findByUsername(USERNAME_TEST))
                .willThrow(o_O);

        mockMvc.perform(post("/groups/edit/search")
                        .param("groupId", GROUP_ID)
                        .param("search", USERNAME_TEST))
                .andExpect(model().attribute("bodyContent", "edit-group"))
                .andExpect(model().attribute("group", GROUP))
                .andExpect(model().attribute("hasError", true))
                .andExpect(model().attribute("error", o_O.getMessage()))
                .andExpect(status().isOk());

        verify(groupService).findById(Long.valueOf(GROUP_ID));
        verify(userService).findByUsername(USERNAME_TEST);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD)
    void should_invite_user_to_group() throws Exception {
        mockMvc.perform(post("/groups/edit/invite")
                        .param("username", USERNAME_TEST)
                        .param("groupId", GROUP_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups/edit/" + GROUP_ID));
    }
}