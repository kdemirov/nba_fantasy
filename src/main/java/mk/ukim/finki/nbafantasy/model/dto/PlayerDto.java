package mk.ukim.finki.nbafantasy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Presentational class for Player entity class.
 */
@AllArgsConstructor
@Builder
@Getter
public class PlayerDto {
    private final Long id;
    private final String name;
    private final String playerImageUrl;
    private final Integer fantasyPointsPerGame;
}
