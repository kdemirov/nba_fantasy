package mk.ukim.finki.nbafantasy.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.data_retrieval.model.ParsedDocument;
import mk.ukim.finki.nbafantasy.data_retrieval.service.RetrievalDataService;
import mk.ukim.finki.nbafantasy.model.Game;
import mk.ukim.finki.nbafantasy.model.Player;
import mk.ukim.finki.nbafantasy.model.Team;
import mk.ukim.finki.nbafantasy.service.GameService;
import mk.ukim.finki.nbafantasy.service.PlayerService;
import mk.ukim.finki.nbafantasy.service.TeamService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller.
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TeamService teamService;
    private final PlayerService playerService;
    private final GameService gameService;
    private final UserService userService;
    private final RetrievalDataService extractDataService;

    /**
     * Returns the admin home page
     *
     * @param model model
     * @return template master-template-admin.html
     */
    @GetMapping("/panel")
    public String getAdminPanel(Model model) {
        model.addAttribute("bodyContent", "admin-panel");
        return "master-template-admin";
    }

    /**
     * Saves a team with given properties and redirects to the admin teams page.
     *
     * @param teamName   team
     * @param imageUrl   team image url
     * @param division   team division
     * @param playersUrl team players url
     * @param model      model needed for the advice if exception occurs
     *                   the save modal should be recreated
     * @return template master-template-admin with body content for teams page
     */
    @PostMapping("/panel/saveTeam")
    public String saveTeam(@RequestParam String teamName,
                           @RequestParam String imageUrl,
                           @RequestParam String division,
                           @RequestParam String playersUrl,
                           Model model) {
        this.teamService.saveTeam(division, teamName, "", playersUrl, imageUrl);
        return "redirect:/admin/panel/teams";
    }

    /**
     * Saves players from parsed html document with given css class name.
     *
     * @param teamId    given team id
     * @param className class name
     * @param model     model needed for the advice so that
     *                  save players modal should be recreated
     * @return template for players admin page
     */
    @PostMapping("/panel/savePlayers")
    public String savePlayers(@RequestParam String teamId,
                              @RequestParam String className,
                              Model model) {
        this.playerService.getPlayers(Long.valueOf(teamId), className);
        return "redirect:/admin/panel/players";
    }

    /**
     * Saves teams from parsed document and modal body in which the user is able to
     * select all the properties of the team.
     *
     * @param model model
     * @return template admin panel home page
     */
    @PostMapping("/panel/getTeams")
    public String fillTeams(Model model) {
        model.addAttribute("bodyContent", "admin-panel");
        ParsedDocument parsedDocument = this.extractDataService.retrieveDataFromUrl(Constants.TEAMS_URL, false);
        model.addAttribute("cssLinks", parsedDocument.getCssNodes());
        model.addAttribute("selectableData", parsedDocument.getParsedBody().outerHtml());
        model.addAttribute("modalBody", "save-team-modal-body");
        return "master-template-admin";

    }

    /**
     * Returns the admin panel teams page.
     *
     * @param model model
     * @return admin panel teams page
     */
    @GetMapping("/panel/teams")
    public String getTeamsPage(Model model) {
        model.addAttribute("bodyContent", "admin-panel-teams");
        model.addAttribute("teams", this.teamService.getAll());
        return "master-template-admin";
    }

    /**
     * Deletes a team with given id and redirects to admin teams page.
     *
     * @param id given id
     * @return admin teams page
     */
    @PostMapping("/panel/teams/delete/{id}")
    public String deleteTeam(@PathVariable Long id) {
        this.teamService.deleteTeam(id);
        return "redirect:/admin/panel/teams";
    }

    /**
     * Parses a html document in order admin to select the needed class
     * for saving the players for the team with the given id.
     *
     * @param id    given team id
     * @param model model
     * @return parsed document with selectable data and modal with form for selecting the class
     */
    @PostMapping("/panel/getPlayers/{id}")
    public String fillPlayers(@PathVariable Long id, Model model) {
        Team t = teamService.findById(id);
        ParsedDocument parsedDocument = extractDataService.retrieveDataFromUrl(Constants.NBA_URL + t.getPlayersUrl(), false);
        model.addAttribute("bodyContent", "admin-panel-teams");
        model.addAttribute("cssLinks", parsedDocument.getCssNodes());
        model.addAttribute("selectableData", parsedDocument.getParsedBody().outerHtml());
        model.addAttribute("modalBody", "save-players-modal-body");
        return "master-template-admin";

    }

    /**
     * Returns the admin players page.
     *
     * @param model model
     * @return admin players page
     */
    @GetMapping("/panel/players")
    public String getPlayersPage(Model model) {
        model.addAttribute("bodyContent", "admin-panel-players");
        model.addAttribute("players", this.playerService.findAll());
        return "master-template-admin";
    }

    /**
     * Returns edit player page.
     *
     * @param id    player id
     * @param model model
     * @return edit page for player with given id
     */
    @GetMapping("/panel/players/edit-page/{id}")
    public String editPlayer(@PathVariable Long id, Model model) {
        Player player = this.playerService.findById(id);
        model.addAttribute("bodyContent", "admin-panel-edit-player");
        model.addAttribute("player", player);
        return "master-template-admin";

    }

    /**
     * Updates players image url with given css class name.
     *
     * @param className given css class
     * @param model     model needed for the advice if exception is thrown
     *                  error message should be shown
     * @return redirects to admin players page
     */
    @PostMapping("/panel/players/fillPlayersImageUrl")
    public String fillPlayerImageUrl(@RequestParam String className, Model model) {
        this.playerService.fillPlayersImageUrl(className);
        return "redirect:/admin/panel/players";
    }

    /**
     * Returns admin games page.
     *
     * @param model model
     * @return games admin page
     */
    @GetMapping("/panel/games")
    public String getGamesPage(Model model) {
        model.addAttribute("bodyContent", "admin-panel-games");
        model.addAttribute("games", this.gameService.findAllUnfinishedGames());
        model.addAttribute("finishedGames", this.gameService.findAllFinishedGames());
        return "master-template-admin";
    }

    /**
     * Parses a html document in order admin to select the needed attributes in modal body
     * form needed for saving a game.
     *
     * @param model model
     * @return parsed html with selectable data and modal
     */
    @GetMapping("/panel/addGame")
    public String addGamePage(Model model) {
        ParsedDocument parsedDocument = extractDataService.retrieveDataFromUrl(Constants.GAMES_URL, true);
        model.addAttribute("bodyContent", "admin-panel-games");
        model.addAttribute("cssLinks", parsedDocument.getCssNodes());
        model.addAttribute("selectableData", parsedDocument.getParsedBody());
        model.addAttribute("modalBody", "save-games-modal-body");
        model.addAttribute("games", this.gameService.findAllUnfinishedGames());
        model.addAttribute("finishedGames", this.gameService.findAllFinishedGames());
        return "master-template-admin";
    }

    /**
     * Saves a game with selected attributes from html parsed document.
     *
     * @param homeTeam       home team name
     * @param awayTeam       away team name
     * @param dayBegin       begin date
     * @param time           time of the game
     * @param week           week of the game since the system is created
     * @param pointsHomeTeam points scored for home team optional if the game is not finished
     * @param pointsAwayTeam points scored for away team optional if the game is not finished
     * @param gameDetailsUrl game details url needed for calculating the fantasy points per player
     *                       optional if the game is not finished
     * @param model          model needed for the advice so that save team modal is
     *                       recreated with valid values and exception is shown for the invalid values.
     * @return redirects to admin panel games
     */
    @PostMapping("/panel/addGame")
    public String addGame(@RequestParam String homeTeam,
                          @RequestParam String awayTeam,
                          @RequestParam String dayBegin,
                          @RequestParam String time,
                          @RequestParam String week,
                          @RequestParam(required = false) String pointsHomeTeam,
                          @RequestParam(required = false) String pointsAwayTeam,
                          @RequestParam(required = false) String gameDetailsUrl,
                          Model model) {
        this.gameService.saveGame(homeTeam, awayTeam, dayBegin, time, week, pointsHomeTeam, pointsAwayTeam,
                gameDetailsUrl);
        return "redirect:/admin/panel/games";
    }

    /**
     * Returns admin edit game page
     *
     * @param id    given game id
     * @param model model
     * @return admin panel edit game template
     */
    @GetMapping("/panel/games/edit/{id}")
    public String getEditGamesPage(@PathVariable Long id,
                                   Model model) {
        Game game = this.gameService.findById(id);
        model.addAttribute("bodyContent", "admin-panel-edit-game");
        model.addAttribute("game", game);
        return "master-template-admin";
    }

    /**
     * Updates a player.
     *
     * @param id          given id
     * @param name        name
     * @param number      number
     * @param height      height
     * @param weightInLbs weight in lbs
     * @param birthDate   birthdate
     * @param age         age
     * @param experience  experience
     * @param school      school
     * @param price       price
     * @return redirects to admin players page
     */
    @PostMapping("/panel/players/edit/{id}")
    public String saveEditedPlayer(@PathVariable Long id,
                                   @RequestParam String name,
                                   @RequestParam Integer number,
                                   @RequestParam String height,
                                   @RequestParam String weightInLbs,
                                   @RequestParam String birthDate,
                                   @RequestParam Integer age,
                                   @RequestParam String experience,
                                   @RequestParam String school,
                                   @RequestParam(required = false) Double price
    ) {
        this.playerService.update(id, name, number, height, weightInLbs, birthDate, age, experience, school, price);
        return "redirect:/admin/panel/players";

    }

    /**
     * Updates a game with given id
     *
     * @param id             given id
     * @param pointsHomeTeam points scored for home team
     * @param pointsAwayTeam points scored for away team
     * @param time           changed time
     * @param gameDetailsUrl game details url needed for calculating fantasy point per player
     * @param model          model needed for the advice so that valid values are saved
     *                       and for the invalid values exception message is shown
     * @return redirects to admin panel games page
     */
    @PostMapping("/panel/games/edit/{id}")
    public String saveEditedGame(@PathVariable Long id,
                                 @RequestParam Integer pointsHomeTeam,
                                 @RequestParam Integer pointsAwayTeam,
                                 @RequestParam String time,
                                 @RequestParam String gameDetailsUrl,
                                 Model model
    ) {
        this.gameService.update(id, pointsHomeTeam, pointsAwayTeam, time, gameDetailsUrl);
        return "redirect:/admin/panel/games";
    }

    /**
     * Calculates fantasy points per player for the game with given id
     *
     * @param id given id
     * @return details with scores for every player page
     */
    @PostMapping("/panel/games/details/{id}")
    public String getGameDetails(@PathVariable Long id) {
        this.gameService.getGameDetails(id);
        return "redirect:/admin/panel/getDetailsForGame/" + id;

    }

    /**
     * Returns calculated fantasy points for players per game with given id
     *
     * @param id    given id
     * @param model model
     * @return admin panel game details template
     */
    @GetMapping("/panel/getDetailsForGame/{id}")
    public String getDetailsGame(@PathVariable Long id, Model model) {
        Game game = this.gameService.findById(id);
        model.addAttribute("bodyContent", "admin-panel-gamedetails");
        model.addAttribute("game", game);
        return "master-template-admin";
    }

    /**
     * Resets weekly fantasy points for every player.
     *
     * @return redirects to admin panel page
     */
    @PostMapping("/panel/resetWeeklyPoints")
    public String resetWeeklyPoints() {
        this.userService.resetWeeklyPoints();
        this.playerService.resetWeeklyPoints();
        return "redirect:/admin/panel";
    }
}
