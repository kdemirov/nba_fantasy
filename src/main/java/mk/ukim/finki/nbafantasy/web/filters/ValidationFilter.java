package mk.ukim.finki.nbafantasy.web.filters;

import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.enumerations.Role;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Checks if user has validated his account if not
 * the user is redirected to verify account page.
 */
@WebFilter
public class ValidationFilter implements Filter {

    private final UserService userService;

    public ValidationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getServletPath();
        User user = null;
        if (path.equals(Constants.MY_TEAM_URL) || path.equals(Constants.TRANSFERS_URL)) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication != null ? authentication.getName() : null;
                user = username != null ? this.userService.findByUsername(username) : null;
            } catch (UsernameNotFoundException o_O) {
                System.out.println(o_O.getMessage());
            }
            if (user != null && !user.isEnabled() && user.getRole().equals(Role.ROLE_USER)) {
                response.sendRedirect(Constants.VERIFY_ACCOUNT_URL);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
