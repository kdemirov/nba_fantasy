package mk.ukim.finki.nbafantasy.model.exceptions;

public class NotificationIdNotFoundException extends RuntimeException {
    public NotificationIdNotFoundException(Long id) {
        super(String.format("Notification with id %d does not exist", id));
    }
}
