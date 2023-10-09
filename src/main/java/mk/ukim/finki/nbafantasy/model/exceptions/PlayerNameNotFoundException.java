package mk.ukim.finki.nbafantasy.model.exceptions;

public class PlayerNameNotFoundException extends RuntimeException {
    public PlayerNameNotFoundException(String name){
        super(String.format("Player with name %s does not exist",name));
    }
}
