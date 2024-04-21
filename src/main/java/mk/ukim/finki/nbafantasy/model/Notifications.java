package mk.ukim.finki.nbafantasy.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Notification entity class.
 */
@NoArgsConstructor
@Entity
@Data
@Getter
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Group group;

    /**
     * Constructor.
     *
     * @param group group
     */
    public Notifications(Group group) {
        this.group = group;
    }
}