package mk.ukim.finki.nbafantasy.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.exceptions.CenterPlayerAlreadyExistException;
import mk.ukim.finki.nbafantasy.model.exceptions.ForwardPlayersAlreadyExistException;
import mk.ukim.finki.nbafantasy.model.exceptions.GuardPlayersAlreadyExistException;
import mk.ukim.finki.nbafantasy.model.exceptions.PlayerAlreadyExistException;
import mk.ukim.finki.nbafantasy.service.GameService;
import mk.ukim.finki.nbafantasy.service.TeamService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Transfers controller.
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/transfers")
public class TransfersController {

    private final TeamService teamService;
    private final UserService userService;
    private final GameService gameService;

    /**
     * Returns transfer page.
     *
     * @param error   exception error
     * @param request http servlet request
     * @param model   model
     * @return transfers template
     */
    @GetMapping
    public String getTransfersPage(@RequestParam(required = false) String error, HttpServletRequest
            request, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        User user = this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("bodyContent", "transfers");
        model.addAttribute("teams", this.teamService.paginationTeams().get(0));
        model.addAttribute("myteam", user.getMyTeam());
        model.addAttribute("notifications", user.getNotifications());
        model.addAttribute("games", this.gameService.findAllUnfinishedGames());
        return "master-template";
    }

    /**
     * Returns all teams with pagination in order the user to select players for his
     * team.
     *
     * @param id      number of paginated results
     * @param request http servlet request
     * @param model   model
     * @return transfers template
     */
    @GetMapping("/{id}")
    public String getPaginationTeams(@PathVariable Integer id, HttpServletRequest request, Model model) {
        User user = this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("bodyContent", "transfers");
        model.addAttribute("teams", this.teamService.paginationTeams().get(id));
        model.addAttribute("myteam", user.getMyTeam());
        model.addAttribute("notifications", user.getNotifications());
        model.addAttribute("games", this.gameService.findAllUnfinishedGames());
        return "master-template";
    }

    /**
     * User makes transfer for player with given id to his own team.
     *
     * @param playerId given player id
     * @param request  http servlet request
     * @return redirects to transfers page
     */
    @PostMapping
    public String makeTransfer(@RequestParam Long playerId, HttpServletRequest request) {
        try {
            this.userService.addPlayer(request.getRemoteUser(), playerId);
        } catch (PlayerAlreadyExistException | CenterPlayerAlreadyExistException | GuardPlayersAlreadyExistException |
                 ForwardPlayersAlreadyExistException e) {
            return "redirect:/transfers?error=" + e.getMessage();
        }
        return "redirect:/transfers";
    }

    /**
     * User removes player with given id from his team.
     *
     * @param id      given player id
     * @param request http servlet request
     * @return redirects to transfers page
     */
    @PostMapping("/delete/{id}")
    public String deletePlayerFromMyTeam(@PathVariable Long id, HttpServletRequest request) {
        this.userService.deletePlayer(request.getRemoteUser(), id);
        return "redirect:/transfers";
    }
}
