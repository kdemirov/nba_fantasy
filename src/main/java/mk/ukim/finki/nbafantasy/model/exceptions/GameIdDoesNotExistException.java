package mk.ukim.finki.nbafantasy.model.exceptions;

public class GameIdDoesNotExistException extends RuntimeException {
    public GameIdDoesNotExistException(Long id) {
        super(String.format("Game with id %d does not exist", id));
    }
}
