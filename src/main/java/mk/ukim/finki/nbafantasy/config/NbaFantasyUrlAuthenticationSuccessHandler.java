package mk.ukim.finki.nbafantasy.config;

import mk.ukim.finki.nbafantasy.model.enumerations.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NbaFantasyUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        String targetUrl = determinateTargetUrl(authentication);
        if (httpServletResponse.isCommitted()) {
            return;
        }
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, targetUrl);
    }

    protected String determinateTargetUrl(final Authentication authentication) {
        Map<Role, String> roleTargetUrlMap = new HashMap<>();
        roleTargetUrlMap.put(Role.ROLE_USER, Constants.MY_TEAM_URL);
        roleTargetUrlMap.put(Role.ROLE_ADMIN, Constants.ADMIN_PANEL_URL);
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority ga : authorities) {
            if (roleTargetUrlMap.containsKey(ga)) {
                return roleTargetUrlMap.get(ga);
            }
        }
        throw new IllegalStateException();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
