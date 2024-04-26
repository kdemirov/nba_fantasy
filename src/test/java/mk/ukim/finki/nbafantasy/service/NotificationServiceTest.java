package mk.ukim.finki.nbafantasy.service;

import mk.ukim.finki.nbafantasy.model.Notifications;
import mk.ukim.finki.nbafantasy.model.exceptions.NotificationNotFoundException;
import mk.ukim.finki.nbafantasy.repository.jpa.NotificationsRepository;
import mk.ukim.finki.nbafantasy.service.impl.NotificationServiceImpls;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * NotificationService Tests.
 */
class NotificationServiceTest {

    private static final Long NOTIFICATION_ID = 20L;

    @InjectMocks
    NotificationServiceImpls notificationService;

    @Mock
    NotificationsRepository notificationsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_find_notification_by_id() {
        //given
        Notifications expectedValue = new Notifications();
        expectedValue.setId(NOTIFICATION_ID);
        when(notificationsRepository.findById(any())).thenReturn(Optional.of(expectedValue));

        //when
        Notifications notification = notificationService.findById(NOTIFICATION_ID);

        //then
        assertEquals(expectedValue.getId(), notification.getId());
    }

    @Test
    void should_not_find_notification_by_id() {
        //given
        when(notificationsRepository.findById(any())).thenReturn(Optional.empty());

        //when
        assertThrows(NotificationNotFoundException.class,
                () -> notificationService.findById(NOTIFICATION_ID));
    }
}