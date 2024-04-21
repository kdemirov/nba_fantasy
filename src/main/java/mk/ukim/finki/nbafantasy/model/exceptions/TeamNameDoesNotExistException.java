package mk.ukim.finki.nbafantasy.model.exceptions;

public class TeamNameDoesNotExistException extends RuntimeException {
    public TeamNameDoesNotExistException(String name) {
        super(String.format("Team with name %s does not exist", name));
    }
}
