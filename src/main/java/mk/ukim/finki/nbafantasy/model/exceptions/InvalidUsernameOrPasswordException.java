package mk.ukim.finki.nbafantasy.model.exceptions;

public class InvalidUsernameOrPasswordException extends RuntimeException{
    public InvalidUsernameOrPasswordException(){
        super("Username and password can not be empty");
    }
}
