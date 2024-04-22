package mk.ukim.finki.nbafantasy.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * Confirmation token used for validating user account.
 */
@NoArgsConstructor
@Entity
@Data
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String confirmationToken;
    @OneToOne
    private User user;

    /**
     * Constructor.
     *
     * @param user user
     */
    public ConfirmationToken(User user) {
        this.user = user;
        this.confirmationToken = UUID.randomUUID().toString();
    }
}
