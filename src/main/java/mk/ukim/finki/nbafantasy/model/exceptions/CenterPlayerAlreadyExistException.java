package mk.ukim.finki.nbafantasy.model.exceptions;

public class CenterPlayerAlreadyExistException extends RuntimeException {
    public CenterPlayerAlreadyExistException(String playerName) {
        super(String.format("You already have center player with name %s", playerName));
    }
}
