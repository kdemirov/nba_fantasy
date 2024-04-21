package mk.ukim.finki.nbafantasy.service.mappers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.Group;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.dto.GroupDto;
import mk.ukim.finki.nbafantasy.model.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper from entity {@link Group} to presentation model {@link GroupDto}
 */
@RequiredArgsConstructor
@Component
public class GroupMapper implements Mapper<Group, GroupDto> {

    private final UserMapper userMapper;

    @Override
    public GroupDto map(Group entity) {
        return GroupDto.builder()
                .id(entity.getId())
                .groupPoints(entity.calculateGroupPoints())
                .name(entity.getName())
                .users(toUsers(entity.getUsers()))
                .build();
    }

    private List<UserDto> toUsers(Set<User> users) {
        return users.stream()
                .map(userMapper::map)
                .collect(Collectors.toList());
    }
}
