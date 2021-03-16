package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.model.Group;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.service.GroupService;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }
    @GetMapping
    public String getGroupPage(HttpServletRequest request, Model model){
        User user=this.userService.findByUsername(request.getRemoteUser());
        model.addAttribute("bodyContent","groups");
        model.addAttribute("myGroups",user.getGroups());
        model.addAttribute("notifications",user.getNotifications());
        return "master-template";
    }
    @GetMapping("/edit/{id}")
    public String getEditGroupPage(@PathVariable Long id,Model model){
        Group group=this.groupService.findById(id);
        model.addAttribute("bodyContent","edit-group");
        model.addAttribute("group",group);
        return "master-template";
    }
    @GetMapping("/add")
    public String getAddGroupPage(Model model){
        model.addAttribute("bodyContent","add-group");
        return "master-template";
    }
    @PostMapping("/add")
    public String saveNewGroup(@RequestParam String name,HttpServletRequest request){
        this.groupService.save(name,request.getRemoteUser());
        return "redirect:/groups";

    }
    @PostMapping("/edit/{id}")
    public String updateEditedPage(@PathVariable Long id,@RequestParam String name){
        this.groupService.update(id,name);
        return "redirect:/groups";
    }
    @PostMapping("/edit/search")
    public String getSearchedUser(@RequestParam Long groupId,@RequestParam String search,Model model){
        Group group=this.groupService.findById(groupId);
        model.addAttribute("bodyContent","edit-group");
        model.addAttribute("group",group);
        User user=null;
        try {
            user = this.userService.findByUsername(search);
        }catch (UsernameNotFoundException e){
            model.addAttribute("hasError",true);
            model.addAttribute("error",e.getMessage());
            return "master-template";
        }
        model.addAttribute("searchedUser",user);
        return "master-template";
    }
    @PostMapping("/edit/invite")
    public String inviteUserToGroup(@RequestParam String username,@RequestParam Long groupId){
        this.groupService.inviteUser(username,groupId);
        return "redirect:/groups/edit/"+groupId;
    }
}
