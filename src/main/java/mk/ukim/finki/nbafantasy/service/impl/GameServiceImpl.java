package mk.ukim.finki.nbafantasy.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.data_retrieval.utils.UrlUtils;
import mk.ukim.finki.nbafantasy.model.Game;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.Team;
import mk.ukim.finki.nbafantasy.model.exceptions.GameIdDoesNotExistException;
import mk.ukim.finki.nbafantasy.model.exceptions.PlayerNameNotFoundException;
import mk.ukim.finki.nbafantasy.repository.jpa.GameRepository;
import mk.ukim.finki.nbafantasy.service.GameService;
import mk.ukim.finki.nbafantasy.service.PlayerService;
import mk.ukim.finki.nbafantasy.service.TeamService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final UserService userService;
    private final TeamService teamService;
    private final WebDriver webDriver;

    @Override
    public Game findById(Long id) {
        return this.gameRepository.findById(id).orElseThrow(() -> new GameIdDoesNotExistException(id));
    }


    @Override
    public Game update(Long id, Integer pointsHomeTeam, Integer pointsAwayTeam, String time, String gameDetailsUrl) {
        Game game = this.gameRepository.findById(id).orElseThrow(() -> new GameIdDoesNotExistException(id));
        game.setPointsHomeTeam(pointsHomeTeam);
        game.setPointsAwayTeam(pointsAwayTeam);
        game.setTime(time);
        game.setGameDetailsUrl(gameDetailsUrl);
        this.gameRepository.save(game);
        return game;
    }

    @Override
    @Transactional
    public void getGameDetails(Long id) {
        Game game = this.findById(id);
        this.playerService.resetPointsPerGame(game);
        UrlUtils.setWebDriver(webDriver);
        String pageSource = UrlUtils.getPageSource(Constants.NBA_URL + game.getGameDetailsUrl(), true);
        Document document = Jsoup.parse(pageSource);
        List<Element> elements = document.select(Constants.TR_ELEMENT).stream().skip(1).collect(Collectors.toList());
        for (Element e : elements) {
            Elements elements1 = e.select(Constants.TD_ELEMENT);
            if (elements1.size() == 21) {
                String playerName = elements1.get(0).select(Constants.GAME_DETAILS_PLAYER_NAME_CLASS).text();
                if (playerName.length() > 0) {
                    Player player = null;
                    try {
                        player = this.playerService.findByName(playerName);
                        Integer personalFouls = Integer.parseInt(elements1.get(18).text());
                        Integer points = Integer.parseInt(elements1.get(19).text());
                        Integer minutesPlayed = Integer.parseInt(elements1.get(1).text().split(Constants.SEMICOLON)[0]);
                        this.playerService.update(player.getId(), personalFouls, points, minutesPlayed);
                        this.userService.calculateUsersFantasyPoints(player);
                    } catch (PlayerNameNotFoundException pe) {
                        System.out.println(pe.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public Game saveGame(String homeTeamName,
                         String awayTeamName,
                         String dayBegin,
                         String time,
                         String week,
                         String pointsHomeTeam,
                         String pointsAwayTeam,
                         String gameDetailsUrl) {
        Team homeTeam = teamService.findByName(homeTeamName);
        Team awayTeam = teamService.findByName(awayTeamName);
        Game game = new Game(homeTeam, awayTeam, week, dayBegin, time);
        if (pointsAwayTeam != null) {
            game.setPointsAwayTeam(Integer.valueOf(pointsAwayTeam));
        }
        if (pointsHomeTeam != null) {
            game.setPointsHomeTeam(Integer.valueOf(pointsHomeTeam));
        }
        if (gameDetailsUrl != null) {
            game.setGameDetailsUrl(gameDetailsUrl);
        }
        return gameRepository.save(game);
    }


    @Override
    public Map<String, List<Game>> findAllFinishedGames() {
        return this.gameRepository.findAllByTimeEquals(Constants.GAME_FINISHED)
                .stream()
                .collect(Collectors.groupingBy(
                        Game::getWeek,
                        () -> new TreeMap<>(Comparator.naturalOrder()),
                        Collectors.toCollection(ArrayList::new)
                ));
    }

    @Override
    public Map<String, List<Game>> findAllUnfinishedGames() {
        return this.gameRepository.findAllByTimeIsNotLike("%" + Constants.GAME_FINISHED + "%")
                .stream()
                .filter(g -> !g.getTime().equals(Constants.INVALID_TIME))
                .collect(Collectors.groupingBy(Game::getWeek,
                        () -> new TreeMap<>(Comparator.naturalOrder()),
                        Collectors.toCollection(ArrayList::new)));
    }
}
