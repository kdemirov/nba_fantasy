package mk.ukim.finki.nbafantasy.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Team entity class.
 */
@NoArgsConstructor
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
    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
    private List<Player> players;

    /**
     * Constructor.
     *
     * @param conference team conference
     * @param name       team name
     * @param code       team code
     * @param playersUrl url for fetching players for the team
     * @param imageUrl   team image url
     */
    public Team(String conference, String name, String code, String playersUrl, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.conference = conference;
        this.code = code;
        this.playersUrl = playersUrl;
        this.players = new ArrayList<>();
    }
}
