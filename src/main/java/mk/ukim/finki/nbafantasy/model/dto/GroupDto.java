package mk.ukim.finki.nbafantasy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * Presentational class for Group entity class.
 */
@AllArgsConstructor
@Builder
@Getter
public class GroupDto implements Serializable {
    public final Long id;
    public final String name;
    public final Double groupPoints;
    public final List<UserDto> users;
}
