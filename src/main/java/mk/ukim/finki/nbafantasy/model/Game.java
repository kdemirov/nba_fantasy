package mk.ukim.finki.nbafantasy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Game entity class.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Game {
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

    /**
     * Constructor.
     *
     * @param homeTeam home team
     * @param awayTeam away team
     * @param week     week since the app is started
     * @param dayBegin date of the game
     * @param time     time of the time
     */
    public Game(Team homeTeam, Team awayTeam, String week, String dayBegin, String time) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.week = week;
        this.dayBegin = dayBegin;
        this.time = time;
        this.pointsAwayTeam = 0;
        this.pointsHomeTeam = 0;
    }
}
