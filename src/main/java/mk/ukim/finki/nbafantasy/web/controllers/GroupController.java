package mk.ukim.finki.nbafantasy.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.Group;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.service.GroupService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Group controller.
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/groups")
@Secured("ROLE_USER")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    /**
     * Returns user groups template
     *
     * @param request request
     * @param model   model
     * @return groups template
     */
    @GetMapping
    public String getGroupPage(HttpServletRequest request, Model model) {
        User user = this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("bodyContent", "groups");
        model.addAttribute("myGroups", user.getGroups());
        model.addAttribute("notifications", user.getNotifications());
        return "master-template";
    }

    /**
     * Returns edit page for a game with given id.
     *
     * @param id    given game
     * @param model model
     * @return edit group template
     */
    @GetMapping("/edit/{id}")
    public String getEditGroupPage(@PathVariable Long id, Model model) {
        Group group = this.groupService.findById(id);
        model.addAttribute("bodyContent", "edit-group");
        model.addAttribute("group", group);
        return "master-template";
    }

    /**
     * Returns add group template.
     *
     * @param model model
     * @return add group template
     */
    @GetMapping("/add")
    public String getAddGroupPage(Model model) {
        model.addAttribute("bodyContent", "add-group");
        return "master-template";
    }

    /**
     * Creates a new user group with given name.
     *
     * @param name    given name
     * @param request http servlet request
     * @return redirects to groups template
     */
    @PostMapping("/add")
    public String saveNewGroup(@RequestParam String name, HttpServletRequest request) {
        this.groupService.save(name, request.getRemoteUser());
        return "redirect:/groups";

    }

    /**
     * Updates group with given id.
     *
     * @param id   given id
     * @param name updated name
     * @return redirects to groups template
     */
    @PostMapping("/edit/{id}")
    public String updateEditedPage(@PathVariable Long id, @RequestParam String name) {
        this.groupService.update(id, name);
        return "redirect:/groups";
    }

    /**
     * Searches a user for inviting in a group with the given id.
     *
     * @param groupId given group id
     * @param search  search string username of the user
     * @param model   model
     * @return edit group template page with founded user or shows message if the user is not found.
     */
    @PostMapping("/edit/search")
    public String getSearchedUser(@RequestParam Long groupId, @RequestParam String search, Model model) {
        Group group = this.groupService.findById(groupId);
        model.addAttribute("bodyContent", "edit-group");
        model.addAttribute("group", group);
        User user = null;
        try {
            user = this.userService.findByUsername(search);
            model.addAttribute("searchedUser", user);
        } catch (UsernameNotFoundException o_O) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", o_O.getMessage());
        }
        return "master-template";
    }

    /**
     * Invites user to a group.
     *
     * @param username username of the invited user
     * @param groupId  group id
     * @return redirect to editing page for the given group id
     */
    @PostMapping("/edit/invite")
    public String inviteUserToGroup(@RequestParam String username, @RequestParam Long groupId) {
        this.groupService.inviteUser(username, groupId);
        return "redirect:/groups/edit/" + groupId;
    }
}
