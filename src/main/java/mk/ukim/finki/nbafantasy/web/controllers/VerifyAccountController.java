package mk.ukim.finki.nbafantasy.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.service.UserService;
import mk.ukim.finki.nbafantasy.service.impl.EmailSenderService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Verify account controller.
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/verify-account")
@Secured("ROLE_USER")
public class VerifyAccountController {

    private final UserService userService;
    private final EmailSenderService emailSenderService;

    /**
     * Return verify account page .
     *
     * @param error exception error
     * @param model model
     * @return verify account template
     */
    @GetMapping
    public String getVerifyPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "verify-account");
        return "master-template";
    }

    /**
     * Verifies user's account with given confirmation code.
     *
     * @param confirmationCode given confirmation code
     * @param request          http servlet request
     * @return redirects to verify account page
     */
    @PostMapping
    public String verifyAccount(@RequestParam String confirmationCode,
                                HttpServletRequest request) {
        User user = this.userService.findByUsername(request.getRemoteUser());
        if (this.userService.verifyAccount(confirmationCode, user)) {
            return "redirect:/myteam?message=Succesfully activated account";
        }
        return "redirect:/verify-account?error=Wrong Code";
    }

    /**
     * Sends email again for requesting another confirmation code.
     *
     * @param request http servlet request
     * @return redirects to verify account page
     */
    @PostMapping("/send")
    public String sendEmailAgain(HttpServletRequest request) {
        User user = this.userService.findByUsername(request.getRemoteUser());
        this.emailSenderService.sendEmail(user);
        return "redirect:/verify-account";
    }
}
