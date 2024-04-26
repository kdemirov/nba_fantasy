package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.AbstractTestClass;
import mk.ukim.finki.nbafantasy.model.Group;
import mk.ukim.finki.nbafantasy.model.Notifications;
import mk.ukim.finki.nbafantasy.model.exceptions.GroupDoesNotExistException;
import mk.ukim.finki.nbafantasy.model.exceptions.UserIsAlreadyInGroupException;
import mk.ukim.finki.nbafantasy.repository.jpa.GroupRepository;
import mk.ukim.finki.nbafantasy.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests for group service.
 */
class GroupServiceTest extends AbstractTestClass {

    private static final Group EXPECTED_GROUP = new Group(NAME);
    private static final String EXPECTED_GROUP_NAME = "GroupName";
    private static final Integer NOTIFICATIONS_SIZE = 1;
    private static final Integer GROUPS_SIZE = 1;
    private static final Double GROUP_POINTS = 5.0;


    @InjectMocks
    GroupServiceImpl groupService;

    @Mock
    GroupRepository groupRepository;

    @Mock
    UserService userService;

    @Mock
    NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_save_group() {
        //given
        when(userService.findByUsername(USERNAME))
                .thenReturn(USER);

        //when
        Group group = groupService.save(NAME, USERNAME);

        //then
        Assertions.assertEquals(NAME, group.getName());
        Assertions.assertEquals(true, USER.getGroups().contains(group));
        Assertions.assertEquals(true, group.getUsers().contains(USER));
    }

    @Test
    void should_find_group_by_id() {
        //given
        when(groupRepository.findById(any())).thenReturn(Optional.of(EXPECTED_GROUP));

        //when
        Group group = groupService.findById(any());

        //then
        Assertions.assertEquals(EXPECTED_GROUP.getName(), group.getName());
    }

    @Test
    void should_not_find_group_with_id() {
        //given
        when(this.groupRepository.findById(any())).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(GroupDoesNotExistException.class,
                () -> groupService.findById(any()));
    }

    @Test
    void should_update_group() {
        //given
        when(groupRepository.findById(any())).thenReturn(Optional.of(EXPECTED_GROUP));

        Group group = groupService.update(any(), EXPECTED_GROUP_NAME);

        Assertions.assertEquals(EXPECTED_GROUP_NAME, group.getName());
    }

    @Test
    void inviteUser() {
        //given
        when(userService.findByUsername(USERNAME)).thenReturn(USER);
        EXPECTED_GROUP.setName(NAME);
        when(groupRepository.findById(any())).thenReturn(Optional.of(EXPECTED_GROUP));

        //when
        groupService.inviteUser(USERNAME, any());

        //then
        Assertions.assertEquals(NOTIFICATIONS_SIZE, USER.getNotifications().size());
        Assertions.assertEquals(NAME, USER.getNotifications().get(0).getGroup().getName());
    }

    @Test
    void should_join_group() {
        //given
        EXPECTED_GROUP.setUsers(new HashSet<>());
        when(userService.findByUsername(USERNAME)).thenReturn(USER);
        Notifications notification = new Notifications(EXPECTED_GROUP);
        when(notificationService.findById(any())).thenReturn(notification);
        when(groupRepository.findById(any())).thenReturn(Optional.of(EXPECTED_GROUP));

        //when
        groupService.joinGroup(any(), ID, USERNAME);

        //then
        Assertions.assertEquals(true, EXPECTED_GROUP.getUsers().contains(USER));
    }

    @Test
    void should_throw_exception_if_user_already_exists_in_a_group() {
        //given
        EXPECTED_GROUP.setUsers(Set.of(USER));
        when(userService.findByUsername(USERNAME)).thenReturn(USER);
        Notifications notification = new Notifications(EXPECTED_GROUP);
        when(notificationService.findById(any())).thenReturn(notification);
        when(groupRepository.findById(any())).thenReturn(Optional.of(EXPECTED_GROUP));

        //when
        Assertions.assertThrows(UserIsAlreadyInGroupException.class,
                () -> groupService.joinGroup(any(), ID, USERNAME));
    }

    @Test
    void should_find_3_best_groups_by_user_which_have_most_fantasy_points() {
        //given
        USER.setFantasyTotalPoints(FIVE_FANTASY_POINTS);
        EXPECTED_GROUP.setUsers(Set.of(USER));
        when(userService.findByUsername(USERNAME)).thenReturn(USER);
        when(groupRepository.findAllByUsersContains(USER)).thenReturn(List.of(EXPECTED_GROUP));

        //when
        List<Group> groups = groupService.findAllGroupsByUser(USERNAME);

        //then
        Assertions.assertEquals(GROUPS_SIZE, groups.size());
        Assertions.assertEquals(GROUP_POINTS, groups.get(0).calculateGroupPoints());
    }
}