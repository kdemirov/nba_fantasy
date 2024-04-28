package mk.ukim.finki.nbafantasy.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.nbafantasy.config.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String imageUrl;
    @NotNull
    @NotEmpty
    private String conference;
    private String code;
    @Pattern(regexp = Constants.URL_REGEX)
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
    }
}
