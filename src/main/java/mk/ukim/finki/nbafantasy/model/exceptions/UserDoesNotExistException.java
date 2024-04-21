package mk.ukim.finki.nbafantasy.model.exceptions;

import mk.ukim.finki.nbafantasy.model.User;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(User user) {
        super(String.format("User with username %s does not exist please register!", user.getUsername()));
    }
}
