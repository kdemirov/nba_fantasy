package mk.ukim.finki.nbafantasy.web.filters;

import mk.ukim.finki.nbafantasy.config.Constants;
import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Checks if user has selected his own team if not the user
 * is redirected to transfers page.
 */
@WebFilter
public class MyTeamFilter implements Filter {

    private final UserService userService;

    public MyTeamFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user = null;
        String path = request.getServletPath();
        if (path.equals(Constants.MY_TEAM_URL)) {
            try {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                user = username != null ? this.userService.findByUsername(username) : null;
            } catch (UsernameNotFoundException o_O) {
                System.out.println(o_O.getMessage());
            }
            if (user != null && user.getMyTeam().size() < Constants.ALLOWED_TEAM_SIZE) {
                response.sendRedirect(Constants.TRANSFERS_URL);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
