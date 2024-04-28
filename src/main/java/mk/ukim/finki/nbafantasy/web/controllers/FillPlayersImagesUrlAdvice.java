package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.service.PlayerService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Aspect for validation the class name
 * for fetching the players image url.
 */
@Aspect
@Component
public class FillPlayersImagesUrlAdvice {

    private static String ERROR_MESSAGE = "Wrong css class name please make sure that you select the class from img element";

    @Autowired
    PlayerService playerService;

    @Around("Pointcuts.adminControllerMethods() && args(className, model)")
    public Object catchWrongClass(ProceedingJoinPoint joinPoint, String className, Model model) {
        Object value;
        try {
            value = joinPoint.proceed();
        } catch (Exception o_O) {
            model.addAttribute("bodyContent", "admin-panel-players");
            model.addAttribute("className", className);
            model.addAttribute("players", playerService.findAll());
            model.addAttribute("error", ERROR_MESSAGE);
            value = "master-template-admin";
        } catch (Throwable o_O) {
            throw new RuntimeException(o_O);
        }
        return value;
    }
}
