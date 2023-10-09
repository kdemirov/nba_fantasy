package mk.ukim.finki.nbafantasy.model.exceptions;

public class PlayerIdDoesNotExistException extends RuntimeException{
    public PlayerIdDoesNotExistException(Long id){
        super(String.format("Player with id %d does not exist",id));
    }
}
