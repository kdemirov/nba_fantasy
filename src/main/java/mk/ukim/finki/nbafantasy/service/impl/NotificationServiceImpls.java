package mk.ukim.finki.nbafantasy.service.impl;

import mk.ukim.finki.nbafantasy.model.Group;
import mk.ukim.finki.nbafantasy.model.Notifications;
import mk.ukim.finki.nbafantasy.model.exceptions.NotificationNotFoundException;
import mk.ukim.finki.nbafantasy.repository.jpa.NotificationsRepository;
import mk.ukim.finki.nbafantasy.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpls implements NotificationService {
    private final NotificationsRepository notificationsRepository;

    public NotificationServiceImpls(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @Override
    public Notifications findById(Long id) {
        return this.notificationsRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException(id));
    }

    @Override
    public Notifications save(Group group) {
        Notifications notifications=new Notifications(group);
        this.notificationsRepository.save(notifications);
        return notifications;
    }
}
