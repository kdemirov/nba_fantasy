package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.Group;
import mk.ukim.finki.nbafantasy.model.Notifications;

public interface NotificationService {
    Notifications findById(Long id);
    void delete(Long id);
    Notifications save(Group group);
}
