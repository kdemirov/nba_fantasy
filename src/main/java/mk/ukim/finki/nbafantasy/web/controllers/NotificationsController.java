package mk.ukim.finki.nbafantasy.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.exceptions.UserIsAlreadyInGroupException;
import mk.ukim.finki.nbafantasy.service.GroupService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Notification controller
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/notifications")
@Secured("ROLE_USER")
public class NotificationsController {

    private final UserService userService;
    private final GroupService groupService;

    /**
     * Returns notifications page.
     *
     * @param error   exception error
     * @param request http servlet request
     * @param model   model
     * @return returns notifications template
     */
    @GetMapping
    public String getNotificationsPage(@RequestParam(required = false) String error, HttpServletRequest request, Model model) {
        if (error != null && error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "notifications");
        model.addAttribute("notifications", userService.fetchNotification(request.getRemoteUser()));
        return "master-template";
    }

    /**
     * User accepts invitation to a group with a given id.
     *
     * @param id             group id
     * @param notificationId notification id
     * @param request        http servlet request
     * @return redirects to notifications page
     */
    @PostMapping("/accept/{id}")
    public String acceptInvitedGroup(@PathVariable Long id, @RequestParam Long notificationId, HttpServletRequest request) {
        try {
            groupService.joinGroup(id, notificationId, request.getRemoteUser());
        } catch (UserIsAlreadyInGroupException o_O) {
            return "redirect:/notifications?error" + o_O.getMessage();
        }
        return "redirect:/notifications";
    }

    /**
     * User declines invitation to a group with a given id.
     *
     * @param id             group id
     * @param notificationId notification id
     * @param request        http servlet request
     * @return redirects to notifications page
     */
    @PostMapping("/decline/{id}")
    public String declineInvitedGroup(@PathVariable Long id, @RequestParam Long notificationId, HttpServletRequest request) {
        this.groupService.declineInvitedGroup(id, notificationId, request.getRemoteUser());
        return "redirect:/notifications";
    }
}
