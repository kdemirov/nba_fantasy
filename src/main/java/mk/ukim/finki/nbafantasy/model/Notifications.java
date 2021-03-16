package mk.ukim.finki.nbafantasy.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Group group;
    public Notifications(){}
    public Notifications(Group group){
        this.group=group;
    }
}
