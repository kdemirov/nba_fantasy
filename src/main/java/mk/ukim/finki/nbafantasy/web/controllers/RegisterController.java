package mk.ukim.finki.nbafantasy.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.enumerations.Role;
import mk.ukim.finki.nbafantasy.model.exceptions.InvalidArgumentException;
import mk.ukim.finki.nbafantasy.model.exceptions.UsernameAlreadyExistException;
import mk.ukim.finki.nbafantasy.service.UserService;
import mk.ukim.finki.nbafantasy.service.impl.EmailSenderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Register controller.
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/register")
public class RegisterController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;


    /**
     * Returns register page.
     *
     * @param error exception error
     * @param model model
     * @return registers template
     */
    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "register");
        return "master-template";
    }

    /**
     * Registers the user and sends confirmation token message to it's provided email
     * in order to verify his account.
     *
     * @param username username
     * @param password password
     * @param email    email
     * @param name     name
     * @param surname  surname
     * @param model    model
     * @return if the registration is successful redirects to login page if not exception error is shown
     */
    @PostMapping
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam String name,
                               @RequestParam String surname,
                               Model model) {
        User user = null;
        try {
            user = this.userService.register(username, password, email, name, surname, Role.ROLE_USER);
            emailSenderService.sendEmail(user);
        } catch (UsernameAlreadyExistException | InvalidArgumentException O_o) {
            return "redirect:/register?error=" + O_o.getMessage();
        }
        model.addAttribute("bodyContent", "login");
        return "master-template";
    }
}
