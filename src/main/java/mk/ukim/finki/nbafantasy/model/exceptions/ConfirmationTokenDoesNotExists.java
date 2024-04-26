package mk.ukim.finki.nbafantasy.model.exceptions;

public class ConfirmationTokenDoesNotExists extends RuntimeException {
    public ConfirmationTokenDoesNotExists(String username) {
        super(String.format("Confirmation token for user with username: %s does not exists", username));
    }
}