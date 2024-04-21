package mk.ukim.finki.nbafantasy.model.exceptions;

public class GroupIdDoesNotExistException extends RuntimeException {
    public GroupIdDoesNotExistException(Long id) {
        super(String.format("Group with id %d does not exist!", id));
    }
}
