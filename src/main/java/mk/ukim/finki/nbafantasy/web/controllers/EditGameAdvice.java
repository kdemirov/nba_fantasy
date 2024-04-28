package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.model.Game;
import mk.ukim.finki.nbafantasy.service.GameService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Aspect for validation the attributes of edited game.
 */
@Aspect
@Component
public class EditGameAdvice {

    private static final String ERROR_MESSAGE = "Game details url should start with '/' exclude https:";

    @Autowired
    GameService gameService;

    @Around("Pointcuts.adminControllerMethods() && args(id, pointsHomeTeam, pointsAwayTeam, time, gameDetailsUrl, model)")
    public Object constraintViolationException(ProceedingJoinPoint joinPoint,
                                               Long id,
                                               Integer pointsHomeTeam,
                                               Integer pointsAwayTeam,
                                               String time,
                                               String gameDetailsUrl,
                                               Model model) throws Throwable {
        Object value;
        try {
            value = joinPoint.proceed();
        } catch (RuntimeException o_O) {
            String message = o_O.getMessage();
            Game game = gameService.findById(id);

            if (time != null || !time.isEmpty()) {
                game.setTime(time);
            }

            if (pointsHomeTeam != null) {
                game.setPointsHomeTeam(pointsHomeTeam);
            }

            if (pointsAwayTeam != null) {
                game.setPointsAwayTeam(pointsAwayTeam);
            }

            if (!gameDetailsUrl.matches(Constants.URL_REGEX)) {
                message = ERROR_MESSAGE;
            }

            model.addAttribute("game", game);
            model.addAttribute("error", message);
            model.addAttribute("bodyContent", "admin-panel-edit-game");
            value = "master-template-admin";
        } catch (Throwable o_O) {
            throw new RuntimeException(o_O);
        }
        return value;
    }
}