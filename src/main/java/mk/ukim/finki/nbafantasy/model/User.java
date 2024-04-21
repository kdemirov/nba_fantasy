package mk.ukim.finki.nbafantasy.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.nbafantasy.model.enumerations.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User entity class.
 */
@NoArgsConstructor
@Data
@Entity
@Table(name = "fantasy_users")
public class User implements UserDetails {
    @Id
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Group> groups = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notifications> notifications;
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = false;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Player> myTeam;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private Integer fantasyWeeklyPoints;
    private Integer fantasyTotalPoints;

    /**
     * Constructor.
     *
     * @param username username
     * @param password encoded password
     * @param email    email
     * @param name     name
     * @param surname  surname
     * @param role     role
     */
    public User(String username, String password, String email, String name, String surname, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.fantasyWeeklyPoints = 0;
        this.fantasyTotalPoints = 0;
    }

    public void resetWeeklyPoint() {
        this.fantasyWeeklyPoints = 0;
    }

    /**
     * Calculates total fantasy points.
     *
     * @param points points per game
     */
    public void setTotalFantasyPoints(Integer points) {
        this.fantasyTotalPoints += points;
    }

    /**
     * Calculates weekly fantasy points.
     *
     * @param points points per game
     */
    public void setWeeklyFantasyPoints(Integer points) {
        this.fantasyWeeklyPoints += points;
    }

    /**
     * Calculates fantasy points.
     *
     * @param points points per game
     */
    public void calculateFantasyPoints(Integer points) {
        setWeeklyFantasyPoints(points);
        setTotalFantasyPoints(points);
    }

    /**
     * Finds center player of user's team.
     *
     * @return optional of {@link Player}
     */
    public Optional<Player> getCenterPlayer() {
        return this.myTeam.stream()
                .filter(p -> p.getPosition().equals("C") || p.getPosition().equals("C-F")).findFirst();
    }

    /**
     * Finds forward players of user's team.
     *
     * @return optional of {@link Player}
     */
    public List<Player> getForwardPlayers() {
        return this.myTeam.stream()
                .filter(p -> p.getPosition().equals("F") || p.getPosition().equals("F-C"))
                .collect(Collectors.toList());
    }

    /**
     * Finds  guard players from user's team.
     *
     * @return optional of {@link Player}
     */
    public List<Player> getGuardPlayers() {
        return this.myTeam
                .stream().filter(p -> p.getPosition().equals("G") || p.getPosition().equals("G-F") || p.getPosition().equals("F-G"))
                .collect(Collectors.toList());
    }

    /**
     * Removes notification.
     *
     * @param notification notification for removal
     */
    public void removeNotification(Notifications notification) {
        this.notifications = this.notifications
                .stream()
                .filter(n -> !n.getId().equals(notification.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Removes player from user's team.
     *
     * @param player player for removal
     */
    public void removePlayerFromMyTeam(Player player) {
        this.myTeam = myTeam.stream()
                .filter(p -> !p.getId().equals(player.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(this.role);
    }
}
