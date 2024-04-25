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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Fantasy user controller.
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
@Secured("ROLE_USER")
public class UsersController {

    private final UserService userService;
    private final GameService gameService;
    private final GroupService groupService;

    /**
     * Returns user's with given username team page.
     *
     * @param username given username
     * @param request  http servlet request
     * @param model    model
     * @return my team template with the team from the user with given username
     */
    @GetMapping("/{username}")
    public String getUsersTeamPage(@PathVariable String username, HttpServletRequest request, Model model) {
        User userForDisplaying = this.userService.findByUsername(username);
        User user = this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("bodyContent", "myteam");
        model.addAttribute("user", userForDisplaying);
        model.addAttribute("notifications", user.getNotifications());
        model.addAttribute("games", gameService.findAllFinishedGames());
        model.addAttribute("groups", this.groupService.findAllGroupsByUser(userForDisplaying.getUsername()));
        return "master-template";
    }
}
