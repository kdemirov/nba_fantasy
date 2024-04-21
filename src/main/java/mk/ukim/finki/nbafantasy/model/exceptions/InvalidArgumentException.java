package mk.ukim.finki.nbafantasy.model.exceptions;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException() {
        super("All fields are required ");
    }
}
