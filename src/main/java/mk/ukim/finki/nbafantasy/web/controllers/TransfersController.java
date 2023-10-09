package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.exceptions.CenterPlayerAlreadyExistException;
import mk.ukim.finki.nbafantasy.model.exceptions.ForwardPlayersAlreadyExistException;
import mk.ukim.finki.nbafantasy.model.exceptions.GuardPlayersAlreadyExistException;
import mk.ukim.finki.nbafantasy.model.exceptions.PlayerAlreadyExistException;
import mk.ukim.finki.nbafantasy.service.GameService;
import mk.ukim.finki.nbafantasy.service.PlayerService;
import mk.ukim.finki.nbafantasy.service.TeamService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/transfers")
public class TransfersController {
    private final TeamService teamService;
    private final UserService userService;
    private final PlayerService playerService;
    private final GameService gameService;

    public TransfersController(TeamService teamService, UserService userService, PlayerService playerService, GameService gameService) {
        this.teamService = teamService;
        this.userService = userService;
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @GetMapping
    public String getTransfersPage(@RequestParam (required = false) String error,HttpServletRequest
            request, Model model){
        if(error!=null&&!error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }
        User user=this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("bodyContent","transfers");
        model.addAttribute("teams",this.teamService.paginationTeams().get(0));
        model.addAttribute("myteam",user.getMyTeam());
        model.addAttribute("notifications",user.getNotifications());
        model.addAttribute("games",this.gameService.findAllUnfinishedGames());
        return "master-template";
    }
    @GetMapping("/{id}")
    public String getPaginationTeams(@PathVariable Integer id,HttpServletRequest request,Model model){
        User user=this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("bodyContent","transfers");
        model.addAttribute("teams",this.teamService.paginationTeams().get(id));
        model.addAttribute("myteam",user.getMyTeam());
        model.addAttribute("notifications",user.getNotifications());
        model.addAttribute("games",this.gameService.findAllUnfinishedGames());
        return "master-template";
    }
    @PostMapping
    public String makeTransfer(@RequestParam Long playerId,HttpServletRequest request ){
        try {
            this.userService.addPlayer(request.getRemoteUser(), playerId);
        }catch (PlayerAlreadyExistException |CenterPlayerAlreadyExistException |GuardPlayersAlreadyExistException | ForwardPlayersAlreadyExistException e){
            return "redirect:/transfers?error="+e.getMessage();
        }
        return "redirect:/transfers";
    }
    @PostMapping("/delete/{id}")
    public String deletePlayerFromMyTeam(@PathVariable Long id,HttpServletRequest request){
        this.userService.deletePlayer(request.getRemoteUser(),id);
        return "redirect:/transfers";
    }
}
