package mk.ukim.finki.nbafantasy.model.exceptions;

public class UsernameAlreadyExistException extends RuntimeException {
    public UsernameAlreadyExistException(String username) {
        super(String.format("Username %s already exist", username));
    }
}
