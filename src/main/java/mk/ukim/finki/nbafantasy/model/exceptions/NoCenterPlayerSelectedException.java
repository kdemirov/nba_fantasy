package mk.ukim.finki.nbafantasy.model.exceptions;

public class NoCenterPlayerSelectedException extends RuntimeException {
    public NoCenterPlayerSelectedException(){
        super("Please Select Center Player");
    }
}
