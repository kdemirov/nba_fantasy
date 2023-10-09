package mk.ukim.finki.nbafantasy.model;

import lombok.Data;
import mk.ukim.finki.nbafantasy.model.enumerations.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name="fantasy_users")
public class User implements UserDetails,Observer {
    @Id
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    @ManyToMany
    private List<Group> groups=new ArrayList<>();
    @OneToMany
    private List<Notifications> notifications;


    private boolean isAccountNonExpired=true;
    private boolean isAccountNonLocked=true;
    private boolean isCredentialsNonExpired=true;
    private boolean isEnabled=false;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Player> myTeam;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private Integer fantasyWeeklyPoints;
    private Integer fantasyTotalPoints;

    public User(){}
    public User(String username,String password,String email,String name,String surname,Role role){
        this.username=username;
        this.password=password;
        this.email=email;
        this.name=name;
        this.surname=surname;
        this.role=role;
        this.fantasyWeeklyPoints=0;
        this.fantasyTotalPoints=0;
    }
    public void resetWeeklyPoint(){
        this.fantasyWeeklyPoints=0;
    }
    public void setTotalFantasyPoints(Integer points){
        this.fantasyTotalPoints+=points;
    }
    public void setWeeklyFantasyPoints(Integer points){
        this.fantasyWeeklyPoints+=points;
    }
    public void calculateFantasyPoints(Integer points){
        setWeeklyFantasyPoints(points);
        setTotalFantasyPoints(points);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
    public Optional<Player> getCenterPlayer(){
        return this.myTeam.stream()
                .filter(p->p.getPosition().equals("C")||p.getPosition().equals("C-F")).findFirst();
    }
    public List<Player> getForwardPlayers(){
        return this.myTeam.stream()
                .filter(p->p.getPosition().equals("F")||p.getPosition().equals("F-C"))
                .collect(Collectors.toList());
    }
    public List<Player> getGuardPlayers(){
        return this.myTeam
                .stream().filter(p->p.getPosition().equals("G")||p.getPosition().equals("G-F")||p.getPosition().equals("F-G"))
                .collect(Collectors.toList());
    }

    public boolean getIsEnabled() {
        return this.isEnabled;
    }

    @Override
    public void update(Notifications notifications) {
        this.notifications.add(notifications);
    }
}
