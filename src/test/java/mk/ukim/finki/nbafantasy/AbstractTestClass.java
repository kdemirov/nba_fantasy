package mk.ukim.finki.nbafantasy;

import mk.ukim.finki.nbafantasy.model.*;
import mk.ukim.finki.nbafantasy.model.enumerations.Role;

import java.util.List;
import java.util.Map;

/**
 * Abstract controller test class.
 */
public class AbstractTestClass {
    protected static final String LOGIN_URL = "http://localhost/login";
    protected static final String USERNAME = "kdemirov";
    protected static final String PASSWORD = "test";
    protected static final String EMAIL = "test@example.com";
    protected static final String NAME = "Test";
    protected static final String SURNAME = "Test";
    protected static final Role ROLE = Role.ROLE_USER;
    protected static final String ADMIN_ROLE = "ADMIN";
    protected static final User USER = new User(USERNAME, PASSWORD, EMAIL, NAME, SURNAME, ROLE);
    protected static final String ADMIN_USERNAME = "admin";
    protected static final String USERNAME_TEST = "test";
    protected static final User TEST_USER = new User(USERNAME_TEST, PASSWORD, EMAIL, NAME, SURNAME, ROLE);
    protected static final String PLAYER_ID = "300";
    protected static final String CENTER_PLAYER = "Center Player";
    protected static final String FORWARD_LEFT_PLAYER = "Forward Left";
    protected static final String FORWARD_RIGHT_PLAYER = "Forward Right";
    protected static final String GUARD_LEFT_PLAYER = "Guard Left";
    protected static final String GUARD_RIGHT_PLAYER = "Guard Right";
    protected static final Integer PLAYER_NUMBER = 1;
    protected static final String CENTER_PLAYER_POSITION = "C";
    protected static final String GUARD_PLAYER_POSITION = "G";
    protected static final String FORWARD_PLAYER_POSITION = "F";
    protected static final String HEIGHT = "185";
    protected static final String WEIGHT_LBS = "45";
    protected static final String BIRTH_DATE = "01.01.1994";
    protected static final Integer AGE = 31;
    protected static final String EXPERIENCE = "R";
    protected static final String SCHOOL = "School";
    protected static final String TEAM_NAME = "TEAMNAME";
    protected static final String IMAGE_URL = "/imageUrl";
    protected static final String DIVISION = "division";
    protected static final String PLAYERS_URL = "/playerUrl";
    protected static final Team TEAM = new Team(DIVISION, TEAM_NAME, "", PLAYERS_URL, IMAGE_URL);
    protected static final String DAY_BEGIN = "Tuesday 14 May";
    protected static final String WEEK = "1";
    protected static final String TIME = "1:30 PM ET";
    protected static final Integer POINTS_FOR_TEAM = 111;
    protected static final String GAME_DETAILS_URL = "/gameDetails/boxScore";
    protected static final String GAME_ID = "420";
    protected static final Game GAME = new Game(TEAM, TEAM, WEEK, DAY_BEGIN, TIME);
    protected static Map<String, List<Game>> gamesByWeek = Map.of("1", List.of());
    protected static final String GROUP_ID = "100";
    protected static final Group GROUP = new Group(NAME);
    protected static final Long ID = 20L;
    protected static final Integer FIVE_FANTASY_POINTS = 5;
    protected static final String GROUP_NAME = "GroupName";

    protected static List<Player> createPlayers() {
        String imageUrl = "/imageUrl";
        List<Player> players = List.of(
                new Player(CENTER_PLAYER, PLAYER_NUMBER, CENTER_PLAYER_POSITION, HEIGHT, WEIGHT_LBS, BIRTH_DATE, AGE, EXPERIENCE, SCHOOL),
                new Player(FORWARD_LEFT_PLAYER, PLAYER_NUMBER, FORWARD_PLAYER_POSITION, HEIGHT, WEIGHT_LBS, BIRTH_DATE, AGE, EXPERIENCE, SCHOOL),
                new Player(FORWARD_RIGHT_PLAYER, PLAYER_NUMBER, FORWARD_PLAYER_POSITION, HEIGHT, WEIGHT_LBS, BIRTH_DATE, AGE, EXPERIENCE, SCHOOL),
                new Player(GUARD_LEFT_PLAYER, PLAYER_NUMBER, GUARD_PLAYER_POSITION, HEIGHT, WEIGHT_LBS, BIRTH_DATE, AGE, EXPERIENCE, SCHOOL),
                new Player(GUARD_RIGHT_PLAYER, PLAYER_NUMBER, GUARD_PLAYER_POSITION, HEIGHT, WEIGHT_LBS, BIRTH_DATE, AGE, EXPERIENCE, SCHOOL)
        );
        players.forEach(p -> p.setPlayerImageUrl(imageUrl));
        return players;
    }
}
