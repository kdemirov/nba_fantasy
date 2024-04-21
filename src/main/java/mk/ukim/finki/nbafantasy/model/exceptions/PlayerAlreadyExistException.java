package mk.ukim.finki.nbafantasy.model.exceptions;

public class PlayerAlreadyExistException extends RuntimeException {
    public PlayerAlreadyExistException(String playerName) {
        super(String.format("Player with name %s is already taken!", playerName));
    }
}
