package mk.ukim.finki.nbafantasy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * Presentational class for Notifications entity class.
 */
@AllArgsConstructor
@Builder
@Getter
public class NotificationDto implements Serializable {
    private Long id;
    private GroupDto group;
}
