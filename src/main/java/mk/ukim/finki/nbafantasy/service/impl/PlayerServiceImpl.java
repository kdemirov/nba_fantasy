package mk.ukim.finki.nbafantasy.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.data_retrieval.utils.UrlUtils;
import mk.ukim.finki.nbafantasy.model.Game;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.Team;
import mk.ukim.finki.nbafantasy.model.exceptions.PlayerDoesNotExistException;
import mk.ukim.finki.nbafantasy.repository.jpa.PlayerRepository;
import mk.ukim.finki.nbafantasy.service.PlayerService;
import mk.ukim.finki.nbafantasy.service.TeamService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlayerServiceImpl implements PlayerService {

    private final TeamService teamService;
    private final PlayerRepository playerRepository;

    @Override
    public void getPlayers(Long teamId, String tableClass) {
        Team t = this.teamService.findById(teamId);
        String urlTeam = Constants.NBA_URL + t.getPlayersUrl();
        Document d = Jsoup.parse(UrlUtils.getPageSource(urlTeam, false));
        Elements e = d.getElementsByClass(tableClass);
        List<String> elementsFromPlayers = new ArrayList<>();
        List<String> playersUrl = new ArrayList<>();

        for (Element element : e) {
            element.select(Constants.TABLE_ELEMENT)
                    .select(Constants.TABLE_BODY_ELEMENT)
                    .select(Constants.TD_ELEMENT)
                    .forEach(el -> elementsFromPlayers.add(el.text()));
            element.select(Constants.LINK_ELEMENT).forEach(el -> playersUrl.add(el.attr(Constants.HREF_ATTR)));
        }

        List<String> tmp = new ArrayList<>();
        List<Player> players = new ArrayList<>();

        for (String s : elementsFromPlayers) {
            tmp.add(s);
            if (tmp.size() == 10) {
                Player player = Player.factoryPlayer(tmp);
                player.setTeam(t);
                players.add(player);
                tmp = new ArrayList<>();
            }
        }

        for (int i = 0; i < playersUrl.size(); i++) {
            players.get(i).setPlayerUrl(playersUrl.get(i));
        }

        t.setPlayers(players);
        this.playerRepository.saveAll(players);
        this.teamService.update(t);
    }

    @Override
    @Transactional
    public void fillPlayersImageUrl(String className) {
        List<Player> updatedPlayersImageUrl = new ArrayList<>();
        String url = Constants.NBA_URL;
        for (Team t : this.teamService.getAll()) {
            Player p = t.getPlayers().get(0);
            String pageSource = null;
            try {
                pageSource = UrlUtils.getPageSource(url + p.getPlayerUrl(), false);
            } catch (Exception O_o) {
                p = t.getPlayers().get(1);
                pageSource = UrlUtils.getPageSource(url + p.getPlayerUrl(), false);
            }
            Document document = Jsoup.parse(pageSource);
            Elements elements = document.select("." + className);
            elements.forEach(el -> {
                String src = el.attr(Constants.SRC_ATTR);
                String playerName = el.attr(Constants.ALT_ATTR);
                List<Player> players = findByNameStartsWith(playerName.replaceAll(Constants.HEADSHOT, Constants.EMPTY_STRING));
                Player player = players
                        .stream()
                        .filter(pl -> pl.getTeam().getName().equals(t.getName()))
                        .findFirst().orElse(null);
                if (player != null && player.getPlayerImageUrl() == null) {
                    player.setPlayerImageUrl(src);
                    updatedPlayersImageUrl.add(player);
                }
            });
        }
        this.playerRepository.saveAll(updatedPlayersImageUrl);
    }

    @Override
    public List<Player> findAll() {
        return this.playerRepository.findAll();
    }

    @Override
    public Player findById(Long id) {
        return this.playerRepository.findById(id).orElseThrow(() -> new PlayerDoesNotExistException(id));
    }

    @Override
    public Player update(Long id,
                         String name,
                         Integer number,
                         String height,
                         String weightInLbs,
                         String birthDate,
                         Integer age,
                         String experience,
                         String school,
                         double price) {
        Player player = this.playerRepository.findById(id).orElseThrow(() -> new PlayerDoesNotExistException(id));
        player.setName(name);
        player.setNumber(number);
        player.setHeight(height);
        player.setWeightInLbs(weightInLbs);
        player.setBirthDate(birthDate);
        player.setAge(age);
        player.setExperience(experience);
        player.setSchool(school);
        player.setPrice(price);
        this.playerRepository.save(player);
        return player;
    }

    @Override
    public Player findByName(String name) {
        return this.playerRepository.findByNameLikeIgnoreCase(name).orElseThrow(() -> new PlayerDoesNotExistException(name));
    }

    @Override
    public List<Player> findByNameStartsWith(String name) {
        return this.playerRepository.findByNameStartsWith(name);
    }

    @Override
    public Player update(Long id, Integer personalFouls, Integer points, Integer minutesPlayed) {
        Player player = findById(id);
        player.calculateFantasyPointsPerGame(personalFouls, points, minutesPlayed);
        this.playerRepository.save(player);
        return player;
    }

    @Override
    public void resetWeeklyPoints() {
        this.playerRepository.findAll().forEach(p -> {
            p.resetWeeklyPoints();
            this.playerRepository.save(p);
        });
    }

    @Override
    public void resetPointsPerGame(Game game) {
        game.getHomeTeam().getPlayers().forEach(p -> {
            p.resetPointsPerGame();
            this.playerRepository.save(p);
        });
        game.getAwayTeam().getPlayers().forEach(p -> {
            p.resetPointsPerGame();
            this.playerRepository.save(p);
        });
    }
}