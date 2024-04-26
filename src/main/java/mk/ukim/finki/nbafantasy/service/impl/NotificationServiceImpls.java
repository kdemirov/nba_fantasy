package mk.ukim.finki.nbafantasy.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.Notifications;
import mk.ukim.finki.nbafantasy.model.exceptions.NotificationNotFoundException;
import mk.ukim.finki.nbafantasy.repository.jpa.NotificationsRepository;
import mk.ukim.finki.nbafantasy.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpls implements NotificationService {

    private final NotificationsRepository notificationsRepository;

    @Override
    public Notifications findById(Long id) {
        return this.notificationsRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException(id));
    }
}
