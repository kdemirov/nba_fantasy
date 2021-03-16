package mk.ukim.finki.nbafantasy.model.exceptions;

import mk.ukim.finki.nbafantasy.model.Player;

import java.util.List;

public class ForwardPlayersAlreadyExistException  extends RuntimeException{
    public ForwardPlayersAlreadyExistException(){
        super("You already have 2 forward players in your team");
    }
}
