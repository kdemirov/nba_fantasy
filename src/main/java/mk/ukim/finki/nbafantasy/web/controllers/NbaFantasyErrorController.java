package mk.ukim.finki.nbafantasy.web.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Error controller.
 */
@Controller
@RequestMapping("/error")
public class NbaFantasyErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    /**
     * If the page the user searches for is not found the user if it is authenticated is redirected
     * to his team page, else to login page.
     *
     * @param request http servlet request
     * @return login or my team template
     */
    @GetMapping
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return SecurityContextHolder.getContext().getAuthentication() != null
                        ? "redirect:/myteam" : "redirect:/login";
            }
        }
        return "error";
    }
}
