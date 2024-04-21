package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.Group;
import mk.ukim.finki.nbafantasy.model.User;

import java.util.List;

public interface GroupService {
    Group save(String name, String username);
    Group findById(Long id);
    void delete(Long id);
    User addUserInGroup(Long groupId,String username);
    Group update(Long id,String name);

    void inviteUser(String username, Long groupId);

    void joinGroup(Long groupId, Long notificationId, String username);

    void declineInvitedGroup(Long groupId, Long notificationId, String username);
    List<Group> findAllGroupsByUser(String username);


}
