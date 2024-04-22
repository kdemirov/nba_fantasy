package mk.ukim.finki.nbafantasy.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.model.dto.NotificationDto;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Notifications rest controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/notification")
public class NotificationRestController {

    private final UserService userService;

    /**
     * Fetches notification every one minute for the authenticated user.
     *
     * @param request http servlet request
     * @return List of {@link NotificationDto}
     */
    @PostMapping
    public List<NotificationDto> fetchNotifications(HttpServletRequest request) {
        return this.userService.fetchNotification(request.getRemoteUser());
    }
}
