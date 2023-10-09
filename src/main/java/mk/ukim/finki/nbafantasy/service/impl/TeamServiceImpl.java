package mk.ukim.finki.nbafantasy.service.impl;

import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.Team;
import mk.ukim.finki.nbafantasy.model.exceptions.TeamIdDoesNotExistException;
import mk.ukim.finki.nbafantasy.model.exceptions.TeamNameDoesNotExistException;
import mk.ukim.finki.nbafantasy.repository.jpa.TeamRepository;
import mk.ukim.finki.nbafantasy.service.TeamService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TeamServiceImpl implements TeamService {

    private final WebDriver webDriver;
    private final TeamRepository teamRepository;
    private static String URL="https://www.nba.com/standings";

    public TeamServiceImpl(WebDriver webDriver, TeamRepository teamRepository) {

        this.webDriver = webDriver;
        this.teamRepository = teamRepository;
    }

    @Override
    public List<Team> getAll() {
        return this.teamRepository.findAll();
    }

    @Override
    public Team saveTeam(String conference,String name,String code,String playersUrl,String imageUrl) {
        Team team=new Team(conference,name,code,playersUrl,imageUrl);
        this.teamRepository.save(team);
        return team;
    }

    @Override
    public Player addPlayer(Long id,Player player) {
        Team team=findById(id);
        team.getPlayers().add(player);
        this.teamRepository.save(team);
        return player;
    }

    @Override
    public Team findById(Long id) {
        return this.teamRepository.findById(id).orElseThrow(()->new TeamIdDoesNotExistException(id));
    }

    @Override
    public Team findByName(String name) {
        return this.teamRepository.findByName(name).orElseThrow(()->new TeamNameDoesNotExistException(name));
    }

    @Override
    public void getTeams() {
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        webDriver.get(URL);
        Document document= Jsoup.parse(webDriver.getPageSource());
        Elements elements=document.getElementsByTag("tr");
        elements.forEach(e->saveTeam(e.select(".Anchor_complexLink__2NtkO").attr("data-section"),
                e.select(".Anchor_complexLink__2NtkO").attr("data-text"),
                e.select(".Anchor_complexLink__2NtkO").attr("data-content"),
                e.select(".Anchor_complexLink__2NtkO").attr("href"),
                e.select(".TeamLogo_logo__1CmT9").attr("src")));
        getAll().removeIf(t->t.getName()==null||t.getName().equals(""));

    }

    @Override
    public void deleteTeam(Long id) {
        this.teamRepository.deleteById(id);
    }

    @Override
    public Team update(Team team) {
        Team tmpTeam=this.teamRepository.findById(team.getId()).orElseThrow(()->new TeamIdDoesNotExistException(team.getId()));
        tmpTeam.setPlayers(team.getPlayers());
        return this.teamRepository.save(tmpTeam);

    }

    @Override
    public List<List<Team>> paginationTeams() {
        List<List<Team>> paginationTeam=new ArrayList<>();
        List<Team> tmp=new ArrayList<>();
        for(Team t:getAll()){
            if(tmp.size()==5){
                paginationTeam.add(tmp);
                tmp=new ArrayList<>();
            }
            tmp.add(t);
        }
        paginationTeam.add(tmp);
        return paginationTeam;
    }
}
