package mk.ukim.finki.nbafantasy.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Player entity class.
 */
@NoArgsConstructor
@Data
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer number;
    private String position;
    private String height;
    private String weightInLbs;
    private String birthDate;
    private Integer age;
    private String experience;
    private String school;
    private String playerUrl;
    private String playerImageUrl;
    private Integer fantasyPointPerGame;
    private Integer fantasyPointsWeekly;
    private Integer totalFantasyPoints;
    private double price;
    @ManyToOne
    private Team team;

    /**
     * Constructor.
     *
     * @param name        player name
     * @param number      player number
     * @param position    position of the player
     * @param height      player's height
     * @param weightInLbs player's weights in lbs
     * @param birthDate   player's birthdate
     * @param age         player's age
     * @param experience  player's experience
     * @param school      player's school
     */
    public Player(String name,
                  Integer number,
                  String position,
                  String height,
                  String weightInLbs,
                  String birthDate,
                  Integer age,
                  String experience,
                  String school) {
        this.name = name;
        this.number = number;
        this.position = position;
        this.height = height;
        this.weightInLbs = weightInLbs;
        this.birthDate = birthDate;
        this.age = age;
        this.experience = experience;
        this.school = school;
        this.playerUrl = null;
        this.playerImageUrl = null;
        this.fantasyPointPerGame = 0;
        this.fantasyPointsWeekly = 0;
        this.totalFantasyPoints = 0;
        this.price = 0;
    }

    /**
     * Resets weekly points.
     */
    public void resetWeeklyPoints() {
        this.fantasyPointsWeekly = 0;
    }

    /**
     * Resets points per game.
     */
    public void resetPointsPerGame() {
        this.fantasyPointPerGame = 0;
    }

    /**
     * Calculates fantasy points for player from one played game, one tenth of the points he scored,
     * plus one point if the player played longer than 24 minutes and minus points for every personal foul
     * which is multiplied by 0.1;
     *
     * @param personalFouls personal fouls
     * @param points        scored points
     * @param minutesPlayed minutes played
     */
    public void calculateFantasyPointsPerGame(Integer personalFouls, Integer points, Integer minutesPlayed) {
        Double avgPoints = (double) points / 10;
        Double minusPoints = (double) personalFouls * 0.1;

        if (minutesPlayed > 24) {
            avgPoints += 1;
        }

        Double finalPoints = avgPoints - minusPoints;

        if (finalPoints < 0) {
            this.fantasyPointPerGame = 0;
        } else {
            this.fantasyPointPerGame = finalPoints.intValue();
            double rest = finalPoints - finalPoints.intValue();
            if (rest >= 0.5) {
                this.fantasyPointPerGame += 1;
            }
        }

        setFantasyPointsWeekly(this.fantasyPointPerGame);
        setTotalFantasyPoints(this.fantasyPointPerGame);
    }

    /**
     * Factory method for creating player from extracted html data.
     *
     * @param tmp extracted data from html
     * @return {@link Player}
     */
    public static Player factoryPlayer(List<String> tmp) {
        String name = tmp.get(0);
        Integer number = Integer.parseInt(!tmp.get(1).equals("") ? tmp.get(1) : "420");
        String position = tmp.get(2);
        String height = tmp.get(3);
        String weightInLbs = tmp.get(4);
        String birthDate = tmp.get(5);
        Integer age = Integer.parseInt(tmp.get(6));
        String experience = tmp.get(7);
        String school = tmp.get(8);
        return new Player(name, number, position, height, weightInLbs, birthDate, age, experience, school);
    }
}
