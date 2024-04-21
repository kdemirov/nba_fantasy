package mk.ukim.finki.nbafantasy.model.exceptions;

public class ForwardPlayersAlreadyExistException extends RuntimeException {
    public ForwardPlayersAlreadyExistException() {
        super("You already have 2 forward players in your team");
    }
}
