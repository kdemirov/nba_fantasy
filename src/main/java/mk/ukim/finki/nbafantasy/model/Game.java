package mk.ukim.finki.nbafantasy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.nbafantasy.config.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
    @NotNull
    private Team homeTeam;
    @ManyToOne
    @NotNull
    private Team awayTeam;
    @NotNull
    @NotEmpty
    private String dayBegin;
    @NotNull
    @NotEmpty
    private String time;
    @Pattern(regexp = Constants.URL_REGEX)
    private String gameDetailsUrl;
    @NotNull
    @NotEmpty
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
     * @param time     time
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
