package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Jpa repository for {@link Notifications}.
 */
public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
}
