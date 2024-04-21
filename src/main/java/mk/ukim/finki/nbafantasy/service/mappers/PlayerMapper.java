package mk.ukim.finki.nbafantasy.service.mappers;

import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.dto.PlayerDto;
import org.springframework.stereotype.Component;

/**
 * Mapper from entity model {@link Player} to presentation model {@link PlayerDto}.
 */
@Component
public class PlayerMapper implements Mapper<Player, PlayerDto> {
    @Override
    public PlayerDto map(Player entity) {
        return PlayerDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .playerImageUrl(entity.getPlayerImageUrl())
                .fantasyPointsPerGame(entity.getFantasyPointPerGame())
                .build();
    }
}
