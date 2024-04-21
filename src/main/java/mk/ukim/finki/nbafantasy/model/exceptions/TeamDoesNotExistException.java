package mk.ukim.finki.nbafantasy.model.exceptions;

public class TeamDoesNotExistException extends RuntimeException {

    public TeamDoesNotExistException(Long id) {
        super(String.format("Team with id %d does not exist", id));
    }

    public TeamDoesNotExistException(String name) {
        super(String.format("Team with name %s does not exist", name));
    }
}
