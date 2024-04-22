package mk.ukim.finki.nbafantasy.model.exceptions;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(Long id) {
        super(String.format("Notification with id %d does not exist", id));
    }
}
