package mk.ukim.finki.nbafantasy.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Group entity table.
 */
@Entity
@Data
@Table(name = "fantasy_groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    public Group() {
    }

    /**
     * Constructor.
     *
     * @param name group name
     */
    public Group(String name) {
        this.name = name;
    }

    /**
     * Calculates group total points by summing the points for all
     * participants in the group.
     *
     * @return
     */
    public Double calculateGroupPoints() {
        return this.users.stream()
                .mapToDouble(User::getFantasyTotalPoints)
                .sum();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Group group = (Group) object;
        return Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
