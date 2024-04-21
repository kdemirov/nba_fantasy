package mk.ukim.finki.nbafantasy.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.ConfirmationToken;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.enumerations.Role;
import mk.ukim.finki.nbafantasy.model.exceptions.InvalidArgumentException;
import mk.ukim.finki.nbafantasy.model.exceptions.UsernameAlreadyExistException;
import mk.ukim.finki.nbafantasy.service.ConfirmationTokenService;
import mk.ukim.finki.nbafantasy.service.UserService;
import mk.ukim.finki.nbafantasy.service.impl.EmailSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
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
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;
    @Value("${spring.mail.username}")
    private String email;

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
     * Registers the user and sends confirmation toke message to its provided email
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
        } catch (UsernameAlreadyExistException | InvalidArgumentException e) {
            return "redirect:/register?error=" + e.getMessage();
        }
        ConfirmationToken ct = this.confirmationTokenService.save(user);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete registration!");
        mailMessage.setFrom(email);
        mailMessage.setText("Here is the code to confirm your account code:" + ct.getConfirmationToken());
        //emailSenderService.sendEmail(mailMessage);
        model.addAttribute("bodyContent", "login");
        return "master-template";
    }
}
