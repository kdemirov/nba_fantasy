package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.service.GameService;
import mk.ukim.finki.nbafantasy.service.GroupService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/myteam")
public class MyTeamController {
    private final UserService userService;
    private final GameService gameService;
    private final GroupService groupService;

    public MyTeamController(UserService userService, GameService gameService, GroupService groupService) {
        this.userService = userService;
        this.gameService = gameService;
        this.groupService = groupService;
    }

    @GetMapping
    public String getMyTeamPage(HttpServletRequest request, Model model){
        User user=this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("bodyContent","myteam");
        model.addAttribute("user",user);
        model.addAttribute("notifications",user.getNotifications());
        model.addAttribute("games",gameService.findAllFinishedGames());
        model.addAttribute("groups",this.groupService.findAllGroupsByUser(request.getRemoteUser()));
        return "master-template";
    }
}
