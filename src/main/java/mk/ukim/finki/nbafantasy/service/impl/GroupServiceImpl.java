package mk.ukim.finki.nbafantasy.service.impl;

import mk.ukim.finki.nbafantasy.model.Group;
import mk.ukim.finki.nbafantasy.model.Notifications;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.exceptions.GroupIdDoesNotExistException;
import mk.ukim.finki.nbafantasy.model.exceptions.UserIsAlreadyInGroupException;
import mk.ukim.finki.nbafantasy.repository.jpa.GroupRepository;
import mk.ukim.finki.nbafantasy.repository.jpa.UserRepository;
import mk.ukim.finki.nbafantasy.service.GroupService;
import mk.ukim.finki.nbafantasy.service.NotificationService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository, NotificationService notificationService) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Group save(String name, String username) {
        Group group=new Group(name);
        User user=this.userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
        user.getGroups().add(group);
        group.getUsers().add(user);
        this.groupRepository.save(group);
        this.userRepository.save(user);
        return group;
    }

    @Override
    public Group findById(Long id) {
        return this.groupRepository.findById(id).orElseThrow(()->new GroupIdDoesNotExistException(id));
    }

    @Override
    public void delete(Long id) {
        this.groupRepository.deleteById(id);
    }

    @Override
    public User addUserInGroup(Long groupId, String username) {
        Group group=findById(groupId);
        User user=this.userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
        group.getUsers().add(user);
        this.groupRepository.save(group);
        return user;
    }

    @Override
    public Group update(Long id, String name) {
        Group group=findById(id);
        group.setName(name);
        this.groupRepository.save(group);
        return group;
    }

    @Override
    public void inviteUser(String username, Long groupId) {
        User user=this.userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
        registerObserver(groupId,user);
        notifyObservers(groupId);


    }

    @Override
    public void registerObserver(Long id, User user) {
        Group group=findById(id);
        group.register(user);
        this.groupRepository.save(group);
    }

    @Override
    public void removeObserver(Long id, User user) {
        Group group=findById(id);
        group.remove(user);
        this.groupRepository.save(group);
    }

    @Override
    public void notifyObservers(Long id) {
        Group group=findById(id);
        Notifications notifications=this.notificationService.save(group);
        group.notifyObservers(notifications);
        this.groupRepository.save(group);
    }

    @Override
    public void joinGroup(User user, Long id) {
        Group group=findById(id);
        if(group.getUsers().contains(user)){
            throw new UserIsAlreadyInGroupException(group.getName(),user.getUsername());
        }
        group.getUsers().add(user);
        removeObserver(id,user);
        this.groupRepository.save(group);
    }

    @Override
    public void declineInvitedGroup(User user, Long id) {
        removeObserver(id,user);
    }
    //returns 3 top groups
    @Override
    public List<Group> findAllGroupsByUser(String username) {
        User user=this.userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
        return this.groupRepository.findAllByUsersContains(user)
                .stream()
                .sorted(Comparator.comparing(Group::calculateGroupPoints))
                .limit(3)
                .collect(Collectors.toList());
    }


}
