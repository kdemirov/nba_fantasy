package mk.ukim.finki.nbafantasy.model.exceptions;

public class UserIsAlreadyInGroupException extends RuntimeException{
    public UserIsAlreadyInGroupException(String groupName,String username){
        super(String.format("User with username %s is already in group %s",username,groupName));
    }
}
