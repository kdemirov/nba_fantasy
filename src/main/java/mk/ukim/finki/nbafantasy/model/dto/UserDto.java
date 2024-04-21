package mk.ukim.finki.nbafantasy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * Presentational class for User entity class.
 */
@AllArgsConstructor
@Builder
@Getter
public class UserDto implements Serializable {
    public final String username;
    public final String name;
    public final String surname;
    public final PlayerDto centerPlayer;
    public final List<PlayerDto> forwardPlayers;
    public final List<PlayerDto> guardPlayers;
    public final Integer fantasyTotalPoints;
    public final Integer fantasyWeeklyPoints;
}
