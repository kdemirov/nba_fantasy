package mk.ukim.finki.nbafantasy.service.impl;

import mk.ukim.finki.nbafantasy.model.Game;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.Team;
import mk.ukim.finki.nbafantasy.model.exceptions.PlayerIdDoesNotExistException;
import mk.ukim.finki.nbafantasy.model.exceptions.PlayerNameNotFoundException;
import mk.ukim.finki.nbafantasy.repository.jpa.PlayerRepository;
import mk.ukim.finki.nbafantasy.service.PlayerService;
import mk.ukim.finki.nbafantasy.service.TeamService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PlayerServiceImpl implements PlayerService {


    private final TeamService teamService;
    private final WebDriver webDriver;
    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(TeamService teamService, WebDriver webDriver, PlayerRepository playerRepository) {
        this.teamService = teamService;
        this.webDriver = webDriver;
        this.playerRepository = playerRepository;
    }

    @Override
    public void getPlayers() {
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        for(Team t:this.teamService.getAll()) {

            String urlTeam = "https://www.nba.com" + t.getPlayersUrl();
            webDriver.get(urlTeam);
            System.out.println(webDriver.getCurrentUrl());
            Document d = Jsoup.parse(webDriver.getPageSource());
            Elements e = d.getElementsByClass("TeamRoster_content__2cYaB");
            List<String> elementsFromPlayers = new ArrayList<>();
            List<String> playersUrl = new ArrayList<>();
            for (Element element : e) {
                element.select("table").select("tbody").select("td").forEach(el -> elementsFromPlayers.add(el.text()));
                element.select("a").forEach(el -> playersUrl.add(el.attr("href")));
            }
            List<String> tmp = new ArrayList<>();
            List<Player> players = new ArrayList<>();
            int count = 1;//attributes for players
            for (String s : elementsFromPlayers) {
                tmp.add(s);
                if (count == 9) {
                    Player player=Player.factoryPlayer(tmp);
                    player.setTeam(t);
                    this.playerRepository.save(player);
                    players.add(player);
                    tmp = new ArrayList<>();
                    count = 0;
                }
                ++count;
            }
            for (int i = 0; i < playersUrl.size(); i++) {
                players.get(i).setPlayerUrl(playersUrl.get(i));
            }
            t.setPlayers(players);
            this.teamService.update(t);
        }

    }

    @Override
    public void fillPlayerImageUrl(Long id) {
        Player player=findById(id);
        String url="https://www.nba.com";


                webDriver.get(url+player.getPlayerUrl());
                Document document=Jsoup.parse(webDriver.getPageSource());
                Elements elements=document.select(".relative.block.bg-transparent.max-w-screen-xxl.mx-auto").select(".PlayerImage_image__1smob");
                String src=elements.attr("src");
                player.setPlayerImageUrl(src);
                this.playerRepository.save(player);



    }

    @Override
    public List<Player> findAll() {
        return this.playerRepository.findAll();
    }

    @Override
    public Player findById(Long id) {
        return this.playerRepository.findById(id).orElseThrow(()->new PlayerIdDoesNotExistException(id));
    }

    @Override
    public Player update(Long id, String name, Integer number, String height, String weightInLbs, String birthDate, Integer age, String expirience, String school, double price) {
        Player player=this.playerRepository.findById(id).orElseThrow(()->new PlayerIdDoesNotExistException(id));
        player.setName(name);
        player.setNumber(number);
        player.setHeight(height);
        player.setWeightInLbs(weightInLbs);
        player.setBirthDate(birthDate);
        player.setAge(age);
        player.setExpirience(expirience);
        player.setSchool(school);
        player.setPrice(price);
        this.playerRepository.save(player);
        return player;
    }

    @Override
    public Player findByName(String name) {
        return this.playerRepository.findByName(name).orElseThrow(()->new PlayerNameNotFoundException(name));
    }

    @Override
    public Player update(Long id, Integer personalFauls,Integer points,Integer minutesPlayed) {
        Player player=findById(id);
        player.calculateFantasyPointsPerGame(personalFauls,points,minutesPlayed);
        this.playerRepository.save(player);
        return player;
    }

    @Override
    public void resetWeeklyPoints() {
        this.playerRepository.findAll().forEach(p->{
            p.resetWeeklyPoints();
            this.playerRepository.save(p);
        });
    }

    @Override
    public void resetPointsPerGame(Game game) {
        game.getHomeTeam().getPlayers().forEach(p->{
            p.resetPointsPerGame();
            this.playerRepository.save(p) ;
        });
        game.getAwayTeam().getPlayers().forEach(p->{
            p.resetPointsPerGame();
            this.playerRepository.save(p);
        });
    }

}
