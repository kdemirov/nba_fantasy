package mk.ukim.finki.nbafantasy.service.impl;

import mk.ukim.finki.nbafantasy.model.Game;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.Team;
import mk.ukim.finki.nbafantasy.model.exceptions.GameIdDoesNotExistException;
import mk.ukim.finki.nbafantasy.model.exceptions.PlayerNameNotFoundException;
import mk.ukim.finki.nbafantasy.model.exceptions.TeamNameDoesNotExistException;
import mk.ukim.finki.nbafantasy.repository.jpa.GameRepository;
import mk.ukim.finki.nbafantasy.service.GameService;
import mk.ukim.finki.nbafantasy.service.PlayerService;
import mk.ukim.finki.nbafantasy.service.TeamService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;
    private final UserService userService;
    private final TeamService teamService;
    private static String URL="https://www.nba.com/schedule";

    public GameServiceImpl(GameRepository gameRepository, PlayerService playerService, UserService userService, TeamService teamService) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
        this.userService = userService;
        this.teamService = teamService;
    }

    @Override
    public void saveGames() {
        // TODO handle this when data retrieval is implemented correctly
        Document document = Jsoup.parse("");
        Elements elements=document.select(".flex.flex-col.pt-5.border-b.border-concrete");
        for(Element e:elements){
            String day=e.select(".text-sm.font-bold.leading-tight.text-cerulean").text();
            String[] partsForNumberOfGames=e.select(".text-sm.font-medium.leading-tight.text-asphalt").text().split("\\|");
            if(partsForNumberOfGames.length<2){
                continue;
            }
            String week=partsForNumberOfGames[0];
            Integer numberOfGames=Integer.parseInt(partsForNumberOfGames[1].split("\\s")[1]);
            for(int i=0;i<numberOfGames;i++) {
                Elements elements1=e.select(".uppercase.text-sm");
                String time=null;
                if(elements1.size()!=0) {
                     time = elements1.get(i).text();
                }else{
                    time="FINAL";
                }
                String homeTeamName=e.select(".text-sm.flex.justify-between").select("a").get(i*2).text();
                String awayTeamName=e.select(".text-sm.flex.justify-between").select("a").get((i*2) + 1).text();
                Team home=null;
                Team away=null;
                try {
                     home = this.teamService.findByName(homeTeamName);
                     away = this.teamService.findByName(awayTeamName);
                }catch (TeamNameDoesNotExistException ex){
                    System.out.println(ex.getMessage());
                }
                Game game=new Game(home,away,week,day,time);
                this.gameRepository.save(game);


            }
        }


    }

    @Override
    public List<Game> findAll() {
        return this.gameRepository.findAll();
    }

    @Override
    public Game findById(Long id) {
        return this.gameRepository.findById(id).orElseThrow(()->new GameIdDoesNotExistException(id));
    }



    @Override
    public Game update(Long id, Integer pointsHomeTeam, Integer pointsAwayTeam, String time,String gameDetailsUrl) {
        Game game=this.gameRepository.findById(id).orElseThrow(()->new GameIdDoesNotExistException(id));
        game.setPointsHomeTeam(pointsHomeTeam);
        game.setPointsAwayTeam(pointsAwayTeam);
        game.setTime(time);
        game.setGameDetailsUrl(gameDetailsUrl);
        this.gameRepository.save(game);
        return game;
    }

    @Override
    public void getGameDetails(Long id, String gameDetailsUrl) {
        Game game=this.findById(id);
        this.playerService.resetPointsPerGame(game);
        // TODO
        Document document = Jsoup.parse("");
        List<Element> elements = document.select("tr").stream().skip(1).collect(Collectors.toList());
        for(Element e :elements){
            Elements elements1=e.select("td");
            if(elements1.size()==21){
                String playerName=elements1.get(0).select(".hidden").text();
                if(playerName.length()>0) {
                    Player player=null;
                    try {
                        player = this.playerService.findByName(playerName);
                        Integer personalFouls = Integer.parseInt(elements1.get(18).text());
                        Integer points=Integer.parseInt(elements1.get(19).text());
                        Integer minutesPlayed=Integer.parseInt(elements1.get(1).text().split(":")[0]);

                        this.playerService.update(player.getId(),personalFouls,points,minutesPlayed);
                        this.userService.calculateUsersFantasyPoints(player);
                    }catch (PlayerNameNotFoundException pe){
                        System.out.println(pe.getMessage());
                    }


                }
            }
        }
    }





    @Override
    public Map<String,List<Game>> findAllFinishedGames() {

        return this.gameRepository.findAllByTimeEquals("FINAL")
                .stream()
                .collect(Collectors.groupingBy(
                        Game::getWeek,
                        ()->new TreeMap<>(Comparator.naturalOrder()),
                        Collectors.toCollection(ArrayList::new)
                ));
    }

    @Override
    public Map<String,List<Game>> findAllUnfinishedGames() {
        return this.gameRepository.findAllByTimeIsNotLike("%FINAL%")
                .stream()
                .filter(g->!g.getTime().equals("PPD"))
                .collect(Collectors.groupingBy(Game::getWeek,
                        ()->new TreeMap<>(Comparator.naturalOrder()),
                        Collectors.toCollection(ArrayList::new)));
    }


}
