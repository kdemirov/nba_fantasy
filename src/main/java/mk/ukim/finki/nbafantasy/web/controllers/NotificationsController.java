package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.model.Notifications;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.exceptions.UserIsAlreadyInGroupException;
import mk.ukim.finki.nbafantasy.service.GroupService;
import mk.ukim.finki.nbafantasy.service.NotificationService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/notifications")
public class NotificationsController {
    private final UserService userService;
    private final GroupService groupService;
    private final NotificationService notificationService;

    public NotificationsController(UserService userService, GroupService groupService, NotificationService notificationService) {
        this.userService = userService;
        this.groupService = groupService;
        this.notificationService = notificationService;
    }
    @GetMapping
    public String getNotificationsPage(@RequestParam(required = false)String error, HttpServletRequest request, Model model){
        if(error!=null && error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }
        User user=this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("bodyContent","notifications");
        model.addAttribute("notifications",user.getNotifications());
        return "master-template";
    }
    @PostMapping("/accept/{id}")
    public String acceptInvitedGroup(@PathVariable Long id,@RequestParam Long notificationId, HttpServletRequest request){
        User user=this.userService.findByUsername(request.getRemoteUser());
        Notifications notifications=this.notificationService.findById(notificationId);
        try {
            this.groupService.joinGroup(user, id);
        }catch (UserIsAlreadyInGroupException exception){
            return "redirect:/notifications?error"+exception.getMessage();
        }
        this.userService.deleteNotificatiton(notifications,request.getRemoteUser());
        this.notificationService.delete(notificationId);
        return "redirect:/notifications";
    }
    @PostMapping("/decline/{id}")
    public String declineInvitedGroup(@PathVariable Long id,@RequestParam Long notificationId,HttpServletRequest request){
        User user=this.userService.findByUsername(request.getRemoteUser());
        Notifications notifications=this.notificationService.findById(notificationId);
        this.userService.deleteNotificatiton(notifications,request.getRemoteUser());
        this.groupService.declineInvitedGroup(user,id);
        this.notificationService.delete(notificationId);
        return "redirect:/notifications";
    }
}
