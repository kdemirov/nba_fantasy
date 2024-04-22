package mk.ukim.finki.nbafantasy.model.exceptions;

public class GroupDoesNotExistException extends RuntimeException {
    public GroupDoesNotExistException(Long id) {
        super(String.format("Group with id %d does not exist!", id));
    }
}
