package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.data_retrieval.model.ParsedDocument;
import mk.ukim.finki.nbafantasy.data_retrieval.service.RetrievalDataService;
import mk.ukim.finki.nbafantasy.service.GameService;
import mk.ukim.finki.nbafantasy.service.TeamService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Aspect for validation the attributes while
 * saving a game.
 */
@Aspect
@Component
public class AddGameAdvice {

    @Autowired
    RetrievalDataService retrievalDataService;

    @Autowired
    GameService gameService;

    @Autowired
    TeamService teamService;

    @Around("Pointcuts.adminControllerMethods() && args(homeTeam, awayTeam, dayBegin, time, week, pointsHomeTeam, pointsAwayTeam, gameDetailsUrl, model)")
    public Object constrainViolationException(ProceedingJoinPoint joinPoint,
                                              String homeTeam,
                                              String awayTeam,
                                              String dayBegin,
                                              String time,
                                              String week,
                                              String pointsHomeTeam,
                                              String pointsAwayTeam,
                                              String gameDetailsUrl,
                                              Model model) {
        Object value;
        try {
            value = joinPoint.proceed();
        } catch (Exception o_O) {
            String message = "";
            ParsedDocument parsedDocument = retrievalDataService.retrieveDataFromUrl(Constants.GAMES_URL, true);
            model.addAttribute("bodyContent", "admin-panel-games");
            model.addAttribute("cssLinks", parsedDocument.getCssNodes());
            model.addAttribute("selectableData", parsedDocument.getParsedBody());
            model.addAttribute("modalBody", "save-games-modal-body");
            model.addAttribute("games", this.gameService.findAllUnfinishedGames());
            model.addAttribute("finishedGames", this.gameService.findAllFinishedGames());
            message = saveValidValues(homeTeam,
                    awayTeam,
                    dayBegin,
                    time,
                    week,
                    pointsHomeTeam,
                    pointsAwayTeam,
                    gameDetailsUrl,
                    model,
                    message);
            model.addAttribute("error", message);
            value = "master-template-admin";
        } catch (Throwable o_O) {
            throw new RuntimeException(o_O);
        }
        return value;
    }

    private String saveValidValues(String homeTeam,
                                   String awayTeam,
                                   String dayBegin,
                                   String time,
                                   String week,
                                   String pointsHomeTeam,
                                   String pointsAwayTeam,
                                   String gameDetailsUrl,
                                   Model model,
                                   String message) {
        if (teamService.exists(homeTeam)) {
            model.addAttribute("homeTeam", homeTeam);
        } else {
            message += "Home team name should not be null \n";
        }

        if (teamService.exists(awayTeam)) {
            model.addAttribute("awayTeam", awayTeam);
        } else {
            message += "Away team name should not be null \n";
        }

        if (dayBegin != null || !dayBegin.isEmpty()) {
            model.addAttribute("dayBegin", dayBegin);
        } else {
            message += "Game day of beginning should not be null or empty \n";
        }

        if (time != null || !time.isEmpty()) {
            model.addAttribute("time", time);
        } else {
            message += "Time should not be empty or null \n";
        }

        if (week != null || !week.isEmpty()) {
            model.addAttribute("week", week);
        } else {
            message += "Week should not be empty or null \n";
        }

        if (StringUtils.isNumeric(pointsHomeTeam)) {
            model.addAttribute("pointsHomeTeam", pointsHomeTeam);
        } else {
            message += "Points for teams should be numeric values \n";
        }

        if (StringUtils.isNumeric(pointsAwayTeam)) {
            model.addAttribute("pointsAwayTeam", pointsAwayTeam);
        } else {
            message += "Points for teams should be numeric values \n";
        }

        if (gameDetailsUrl != null && gameDetailsUrl.matches(Constants.URL_REGEX)) {
            model.addAttribute("gameDetailsUrl", gameDetailsUrl);
        } else {
            message += "Game details url should start with '/' exclude the part with https \n";
        }

        return message;
    }
}
