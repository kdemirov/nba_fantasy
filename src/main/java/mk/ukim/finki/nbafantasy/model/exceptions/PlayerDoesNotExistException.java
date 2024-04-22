package mk.ukim.finki.nbafantasy.model.exceptions;

public class PlayerDoesNotExistException extends RuntimeException {
    public PlayerDoesNotExistException(Long id) {
        super(String.format("Player with id %d does not exist", id));
    }

    public PlayerDoesNotExistException(String name) {
        super(String.format("Player with name %s does not exist", name));
    }
}
