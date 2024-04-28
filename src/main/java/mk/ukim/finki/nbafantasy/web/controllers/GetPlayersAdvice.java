package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.data_retrieval.model.ParsedDocument;
import mk.ukim.finki.nbafantasy.data_retrieval.service.RetrievalDataService;
import mk.ukim.finki.nbafantasy.model.Team;
import mk.ukim.finki.nbafantasy.service.TeamService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Aspect for validation the css class name
 * for fetching the players for a team.
 */
@Aspect
@Component
public class GetPlayersAdvice {

    private static final String ERROR_MESSAGE = "The player for team %s could not be fetched due to following re" +
            "ason %s please select the current class that contains the table or the parent of the table html element";
    @Autowired
    TeamService teamService;

    @Autowired
    RetrievalDataService extractDataService;

    @Around("Pointcuts.adminControllerMethods() && args(teamId, className, model)")
    public Object getPlayersExceptionHandling(ProceedingJoinPoint joinPoint, String teamId, String className, Model model) {
        Object value;
        try {
            value = joinPoint.proceed();
        } catch (Exception o_O) {
            Team t = teamService.findById(Long.valueOf(teamId));
            ParsedDocument parsedDocument = extractDataService.retrieveDataFromUrl(Constants.NBA_URL + t.getPlayersUrl(), false);
            model.addAttribute("bodyContent", "admin-panel-teams");
            model.addAttribute("cssLinks", parsedDocument.getCssNodes());
            model.addAttribute("selectableData", parsedDocument.getParsedBody().outerHtml());
            model.addAttribute("modalBody", "save-players-modal-body");
            model.addAttribute("className", className);
            model.addAttribute("error", String.format(ERROR_MESSAGE, t.getName(), o_O.getMessage()));
            value = "master-template-admin";
        } catch (Throwable o_O) {
            throw new RuntimeException(o_O);
        }
        return value;
    }

}
