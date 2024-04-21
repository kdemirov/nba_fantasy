package mk.ukim.finki.nbafantasy.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Login controller.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    /**
     * Returns login page.
     *
     * @param error error if the credentials are invalid
     * @param model model
     * @return login template
     */
    @GetMapping
    public String getLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "login");
        return "master-template";
    }
}
