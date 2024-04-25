package mk.ukim.finki.nbafantasy.web.filters;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Web Filter if user is logged in and navigates through url to
 * register page or login page
 */
@RequiredArgsConstructor
@WebFilter
public class RegisterFilter implements Filter {
    private final UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getServletPath();
        if (path.equals(Constants.REGISTER_URL) || path.equals(Constants.LOGIN_URL)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                User user = userService.findByUsername(authentication.getName());
                if (user != null && user.getMyTeam().size() < 5) {
                    response.sendRedirect(Constants.TRANSFERS_URL);
                } else {
                    response.sendRedirect(Constants.MY_TEAM_URL);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
