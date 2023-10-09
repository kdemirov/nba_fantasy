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
    void inviteUser(String username,Long groupId);
    void registerObserver(Long id,User user);
    void removeObserver(Long id,User user);
    void notifyObservers(Long id);
    void joinGroup(User user,Long id);
    void declineInvitedGroup(User user,Long id);
    List<Group> findAllGroupsByUser(String username);


}
