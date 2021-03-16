package mk.ukim.finki.nbafantasy.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="fantasy_groups")
public class Group implements Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToMany
    private List<User> users=new ArrayList<>();
    @OneToMany
    public List<User> observers;
    public Group(){}
    public Group(String name){
        this.name=name;

    }

    @Override
    public void register(User o) {
        this.observers.add(o);
    }

    @Override
    public void remove(User o) {
        this.observers.remove(o);
    }
    public Double calculateGroupPoints(){
       return this.users.stream()
                .mapToDouble(User::getFantasyTotalPoints)
                .sum();
    }
    @Override
    public void notifyObservers(Notifications notifications) {
        this.observers.forEach(o->o.update(notifications));
    }
}
