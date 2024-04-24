package mk.ukim.finki.nbafantasy.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.service.GameService;
import mk.ukim.finki.nbafantasy.service.GroupService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * MyTeam Controller
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/myteam")
@Secured("ROLE_USER")
public class MyTeamController {

    private final UserService userService;
    private final GameService gameService;
    private final GroupService groupService;

    /**
     * Returns user's team page.
     *
     * @param request http servlet request
     * @param model   model
     * @return user's team page
     */
    @GetMapping
    public String getMyTeamPage(HttpServletRequest request, Model model) {
        User user = this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("bodyContent", "myteam");
        model.addAttribute("user", user);
        model.addAttribute("notifications", this.userService.fetchNotification(request.getRemoteUser()));
        model.addAttribute("games", gameService.findAllFinishedGames());
        model.addAttribute("groups", this.groupService.findAllGroupsByUser(request.getRemoteUser()));
        return "master-template";
    }
}
