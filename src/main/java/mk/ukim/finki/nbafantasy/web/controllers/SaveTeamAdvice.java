package mk.ukim.finki.nbafantasy.web.controllers;

import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.data_retrieval.model.ParsedDocument;
import mk.ukim.finki.nbafantasy.data_retrieval.service.RetrievalDataService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Aspect for validation the attributes for
 * saving a team.
 */
@Aspect
@Component
public class SaveTeamAdvice {

    @Autowired
    RetrievalDataService retrievalDataService;

    @Around("Pointcuts.adminControllerMethods() && args(teamName, imageUrl, division, playersUrl, model)")
    public Object constrainViolationAttact(ProceedingJoinPoint joinPoint,
                                           String teamName,
                                           String imageUrl,
                                           String division,
                                           String playersUrl,
                                           Model model) {
        Object value;
        try {
            value = joinPoint.proceed();
        } catch (Exception o_O) {
            String message = "";
            model.addAttribute("bodyContent", "admin-panel");
            ParsedDocument parsedDocument = this.retrievalDataService.retrieveDataFromUrl(Constants.TEAMS_URL, false);
            model.addAttribute("cssLinks", parsedDocument.getCssNodes());
            model.addAttribute("selectableData", parsedDocument.getParsedBody().outerHtml());
            model.addAttribute("modalBody", "save-team-modal-body");

            if (teamName != null || !teamName.isEmpty()) {
                model.addAttribute("teamName", teamName);
            } else {
                message += "Team name should not be empty or null \n";
            }

            if (division != null || !division.isEmpty()) {
                model.addAttribute("division", division);
            } else {
                message += "Team division should not be empty or null \n";
            }

            if (imageUrl != null || !division.isEmpty()) {
                model.addAttribute("imageUrl", imageUrl);
            } else {
                message += "Team image url should not be empty or null \n";
            }

            if (playersUrl.matches(Constants.URL_REGEX)) {
                model.addAttribute("playersUrl", playersUrl);
            } else {
                message += "Team players url should start with '/' exclude https \n";
            }

            model.addAttribute("error", message);
            value = "master-template-admin";
        } catch (Throwable o_O) {
            throw new RuntimeException(o_O);
        }
        return value;
    }
}
