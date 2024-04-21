package mk.ukim.finki.nbafantasy.model.exceptions;

public class TeamIdDoesNotExistException extends RuntimeException {

    public TeamIdDoesNotExistException(Long id) {
        super(String.format("Team with id %d does not exist", id));
    }
}
