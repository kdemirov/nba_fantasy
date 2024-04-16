package mk.ukim.finki.nbafantasy.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.data_retrieval.model.ParsedDocument;
import mk.ukim.finki.nbafantasy.data_retrieval.service.RetrievalDataService;
import mk.ukim.finki.nbafantasy.model.Game;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.service.GameService;
import mk.ukim.finki.nbafantasy.service.PlayerService;
import mk.ukim.finki.nbafantasy.service.TeamService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final TeamService teamService;
    private final PlayerService playerService;
    private final GameService gameService;
    private final UserService userService;
    private final RetrievalDataService extractDataService;

    @GetMapping("/panel")
    public String getAdminPanel(Model model) {
        model.addAttribute("bodyContent", "admin-panel");
        return "master-template-admin";
    }

    @PostMapping("/panel/getTeams")
    public String fillTeams(Model model) {
        model.addAttribute("bodyContent", "admin-panel");
        ParsedDocument parsedDocument = this.extractDataService.retrieveDataFromUrl("https://www.nba.com/teams");
        model.addAttribute("cssLinks", parsedDocument.getCssNodes());
        model.addAttribute("selectableData", parsedDocument.getParsedBody().outerHtml());
        return "master-template-admin";

    }

    @GetMapping("/panel/teams")
    public String getTeamsPage(Model model) {
        model.addAttribute("bodyContent", "admin-panel-teams");
        model.addAttribute("teams", this.teamService.getAll());
        return "master-template-admin";
    }

    @PostMapping("/panel/teams/delete/{id}")
    public String deleteTeam(@PathVariable Long id) {
        this.teamService.deleteTeam(id);
        return "redirect:/admin/panel/teams";
    }

    @PostMapping("/panel/getPlayers")
    public String fillPlayers(Model model) {
        this.playerService.getPlayers();
        model.addAttribute("bodyContent", "admin-panel");
        return "master-template-admin";

    }

    @GetMapping("/panel/players")
    public String getPlayersPage(Model model) {
        model.addAttribute("bodyContent", "admin-panel-players");
        model.addAttribute("players", this.playerService.findAll());
        return "master-template-admin";
    }

    @PostMapping("/panel/players/edit{id}")
    public String editPlayer(@PathVariable Long id, Model model) {
        Player player = this.playerService.findById(id);
        model.addAttribute("bodyContent", "admin-panel-edit-player");
        model.addAttribute("player", player);
        return "master-template-admin";

    }

    @GetMapping("/panel/players/fillPlayerImagesUrl")
    public String fillPlayerImageUrl() {
        for (Player player : this.playerService.findAll()) {
            if (player.getPlayerImageUrl() == null) {
                this.playerService.fillPlayerImageUrl(player.getId());
            }
        }
        return "redirect:/admin/panel/players";
    }

    @PostMapping("/panel/getGames")
    public String fillGames(Model model) {
        this.gameService.saveGames();
        model.addAttribute("bodyContent", "admin-panel");
        return "master-template-admin";

    }

    @GetMapping("/panel/games")
    public String getGamesPage(Model model) {
        model.addAttribute("bodyContent", "admin-panel-games");
        model.addAttribute("games", this.gameService.findAllUnfinishedGames());
        model.addAttribute("finishedGames", this.gameService.findAllFinishedGames());
        return "master-template-admin";
    }

    @GetMapping("panel/players/edit{id}")
    public String getEditPlayerPage(@PathVariable Long id, Model model) {
        Player player = this.playerService.findById(id);
        model.addAttribute("bodyContent", "admin-panel-edit-player");
        model.addAttribute("player", player);
        return "master-template-admin";

    }

    @GetMapping("panel/games/edit/{id}")
    public String getEditGamesPage(@PathVariable Long id, Model model) {
        Game game = this.gameService.findById(id);
        model.addAttribute("bodyContent", "admin-panel-edit-game");
        model.addAttribute("game", game);
        return "master-template-admin";
    }

    @PostMapping("/panel/players/edit/{id}")
    public String saveEditedPlayer(@RequestParam Long id,
                                   @RequestParam String name,
                                   @RequestParam Integer number,
                                   @RequestParam String height,
                                   @RequestParam String weightInLbs,
                                   @RequestParam String birthDate,
                                   @RequestParam Integer age,
                                   @RequestParam String expirience,
                                   @RequestParam String school,
                                   @RequestParam double price
    ) {
        this.playerService.update(id, name, number, height, weightInLbs, birthDate, age, expirience, school, price);
        return "redirect:/admin/panel/players";

    }

    @PostMapping("/panel/games/edit/{id}")
    public String saveEditedGame(@RequestParam Long id,
                                 @RequestParam Integer pointsHomeTeam,
                                 @RequestParam Integer pointsAwayTeam,
                                 @RequestParam String time,
                                 @RequestParam String gameDetailsUrl
    ) {
        this.gameService.update(id, pointsHomeTeam, pointsAwayTeam, time, gameDetailsUrl);
        return "redirect:/admin/panel/games";
    }

    @PostMapping("/panel/games/details/{id}")
    public String getGameDetails(@PathVariable Long id
    ) {
        //canvas size 924px 2062
        Game game = this.gameService.findById(id);
        this.gameService.getGameDetails(id, game.getGameDetailsUrl());


        return "redirect:/admin/panel/getDetailsForGames/" + id;

    }

    @GetMapping("/panel/getDetailsForGames/{id}")
    public String getDetailsGame(@PathVariable Long id, Model model) {
        Game game = this.gameService.findById(id);
        model.addAttribute("bodyContent", "admin-panel-gamedetails");
        model.addAttribute("game", game);
        return "master-template-admin";
    }

    @PostMapping("/panel/resetWeeklyPoints")
    public String resetWeeklyPoints() {
        this.userService.resetWeeklyPoints();
        this.playerService.resetWeeklyPoints();
        return "redirect:/admin/panel";
    }

    @PostMapping("/panel/saveTeam")
    public String saveTeam(@RequestParam String teamName,
                           @RequestParam String imageUrl,
                           @RequestParam String division,
                           @RequestParam String playersUrl) {
        this.teamService.saveTeam(division, teamName, "", playersUrl, imageUrl);
        return "redirect:/admin/panel/teams";
    }
}
