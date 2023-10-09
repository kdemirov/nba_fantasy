package mk.ukim.finki.nbafantasy.model.exceptions;

public class GuardPlayersAlreadyExistException extends RuntimeException{
    public GuardPlayersAlreadyExistException(){
        super("You already have 2 guard players in your team");
    }
}
