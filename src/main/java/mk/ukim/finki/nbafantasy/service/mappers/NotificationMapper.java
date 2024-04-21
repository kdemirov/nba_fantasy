package mk.ukim.finki.nbafantasy.service.mappers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.Notifications;
import mk.ukim.finki.nbafantasy.model.dto.NotificationDto;
import org.springframework.stereotype.Component;

/**
 * Mapper from entity {@link Notifications} to presentation model {@link NotificationDto}.
 */
@RequiredArgsConstructor
@Component
public class NotificationMapper implements Mapper<Notifications, NotificationDto> {

    private final GroupMapper groupMapper;

    @Override
    public NotificationDto map(Notifications entity) {
        return NotificationDto.builder()
                .id(entity.getId())
                .group(groupMapper.map(entity.getGroup()))
                .build();
    }
}
