package mk.ukim.finki.nbafantasy.model;

import lombok.Data;


import javax.persistence.*;

@Data
@Entity
public class Game{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Team homeTeam;
    @ManyToOne
    private Team awayTeam;
    private String dayBegin;
    private String time;
    private String gameDetailsUrl;
    private String week;
    private Integer pointsHomeTeam;
    private Integer pointsAwayTeam;

    public Game(){}
    public Game(Team homeTeam, Team awayTeam,String week,String dayBegin,String time) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.dayBegin=dayBegin;
        this.time=time;
        this.gameDetailsUrl=null;
        this.week=week;
        this.pointsAwayTeam=0;
        this.pointsHomeTeam=0;

    }

}
