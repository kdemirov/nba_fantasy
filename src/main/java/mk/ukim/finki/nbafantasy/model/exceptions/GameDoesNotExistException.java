package mk.ukim.finki.nbafantasy.model.exceptions;

public class GameDoesNotExistException extends RuntimeException {
    public GameDoesNotExistException(Long id) {
        super(String.format("Game with id %d does not exist", id));
    }
}
