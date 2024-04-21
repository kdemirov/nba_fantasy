package mk.ukim.finki.nbafantasy.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.Group;
import mk.ukim.finki.nbafantasy.model.Notifications;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.exceptions.GroupIdDoesNotExistException;
import mk.ukim.finki.nbafantasy.model.exceptions.UserIsAlreadyInGroupException;
import mk.ukim.finki.nbafantasy.repository.jpa.GroupRepository;
import mk.ukim.finki.nbafantasy.service.GroupService;
import mk.ukim.finki.nbafantasy.service.NotificationService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    @Override
    public Group save(String name, String username) {
        Group group = new Group(name);
        User user = userService.findByUsername(username);
        user.getGroups().add(group);
        group.getUsers().add(user);
        this.groupRepository.save(group);
        this.userService.save(user);
        return group;
    }

    @Override
    public Group findById(Long id) {
        return this.groupRepository.findById(id).orElseThrow(() -> new GroupIdDoesNotExistException(id));
    }

    @Override
    public void delete(Long id) {
        this.groupRepository.deleteById(id);
    }

    @Override
    public User addUserInGroup(Long groupId, String username) {
        Group group = findById(groupId);
        User user = userService.findByUsername(username);
        group.getUsers().add(user);
        this.groupRepository.save(group);
        return user;
    }

    @Override
    public Group update(Long id, String name) {
        Group group = findById(id);
        group.setName(name);
        this.groupRepository.save(group);
        return group;
    }

    @Override
    public void inviteUser(String username, Long groupId) {
        Group group = findById(groupId);
        Notifications notification = new Notifications(group);
        User user = userService.findByUsername(username);
        user.getNotifications().add(notification);
        userService.save(user);
    }

    @Override
    public void joinGroup(Long groupId, Long notificationId, String username) {
        User user = userService.findByUsername(username);
        Notifications notification = this.notificationService.findById(notificationId);
        this.userService.deleteNotificatiton(notification, username);
        Group group = findById(groupId);
        if (group.getUsers().contains(user)) {
            throw new UserIsAlreadyInGroupException(group.getName(), user.getUsername());
        }
        group.getUsers().add(user);
        this.groupRepository.save(group);
    }

    @Override
    public void declineInvitedGroup(Long groupId, Long notificationId, String username) {
        User user = this.userService.findByUsername(username);
        Notifications notifications = this.notificationService.findById(notificationId);
        this.userService.deleteNotificatiton(notifications, username);
    }

    @Override
    public List<Group> findAllGroupsByUser(String username) {
        User user = userService.findByUsername(username);
        return this.groupRepository.findAllByUsersContains(user)
                .stream()
                .sorted(Comparator.comparing(Group::calculateGroupPoints))
                .limit(3)
                .collect(Collectors.toList());
    }


}
