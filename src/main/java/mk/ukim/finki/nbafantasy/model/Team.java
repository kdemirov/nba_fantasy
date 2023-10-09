package mk.ukim.finki.nbafantasy.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imageUrl;
    private String conference;
    private String code;
    private String playersUrl;
    @OneToMany(mappedBy = "team",fetch = FetchType.EAGER)
    private List<Player> players;

    public Team(){}

    public Team(String conference,String name,String code,String playersUrl,String imageUrl){
       this.name=name;
       this.imageUrl=imageUrl;
       this.conference=conference;
       this.code=code;
       this.playersUrl=playersUrl;
       this.players=new ArrayList<>();
    }
}
