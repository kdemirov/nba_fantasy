package mk.ukim.finki.nbafantasy.service.mappers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.dto.PlayerDto;
import mk.ukim.finki.nbafantasy.model.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper from entity model {@link User} to presentational mode {@link UserDto}.
 */
@RequiredArgsConstructor
@Component
public class UserMapper implements Mapper<User, UserDto> {

    private final PlayerMapper playerMapper;

    @Override
    public UserDto map(User entity) {
        return UserDto.builder()
                .username(entity.getUsername())
                .name(entity.getName())
                .centerPlayer(entity.getCenterPlayer().isPresent() ?
                        playerMapper.map(entity.getCenterPlayer().get())
                        : null)
                .forwardPlayers(toForwardPlayers(entity.getForwardPlayers()))
                .guardPlayers(toGuardPlayers(entity.getGuardPlayers()))
                .fantasyTotalPoints(entity.getFantasyTotalPoints())
                .fantasyWeeklyPoints(entity.getFantasyWeeklyPoints())
                .build();
    }

    private List<PlayerDto> toGuardPlayers(List<Player> guardPlayers) {
        return guardPlayers.stream()
                .map(playerMapper::map)
                .collect(Collectors.toList());
    }

    private List<PlayerDto> toForwardPlayers(List<Player> forwardPlayers) {
        return forwardPlayers.stream()
                .map(playerMapper::map)
                .collect(Collectors.toList());
    }
}
