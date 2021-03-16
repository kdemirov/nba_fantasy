package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.service.GameService;
import mk.ukim.finki.nbafantasy.service.GroupService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final GameService gameService;
    private final GroupService groupService;

    public UsersController(UserService userService, GameService gameService, GroupService groupService) {
        this.userService = userService;
        this.gameService = gameService;
        this.groupService = groupService;
    }

    @GetMapping("/{username}")
    public String getUsersTeamPage(@PathVariable String username, HttpServletRequest request, Model model){
        User userForDisplaying=this.userService.findByUsername(username);
        User user=this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("bodyContent","myteam");
        model.addAttribute("user",userForDisplaying);
        model.addAttribute("notifications",user.getNotifications());
        model.addAttribute("games",gameService.findAllFinishedGames());
        model.addAttribute("groups",this.groupService.findAllGroupsByUser(userForDisplaying.getUsername()));
        return "master-template";
    }
}
