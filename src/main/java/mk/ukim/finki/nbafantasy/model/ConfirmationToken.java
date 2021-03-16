package mk.ukim.finki.nbafantasy.model;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String confirmationToken;
    @OneToOne
    private User user;
    public ConfirmationToken(){}
    public ConfirmationToken(User user){
        this.user=user;
        this.confirmationToken= UUID.randomUUID().toString();
    }
}
