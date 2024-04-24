package mk.ukim.finki.nbafantasy.config;

import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.model.enumerations.Role;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
@Primary
public class AuthenticationProviderMock implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = getUser(authentication.getPrincipal().toString());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), "", getAuthorities(user));
        usernamePasswordAuthenticationToken.setDetails(user);
        return usernamePasswordAuthenticationToken;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return Collections.singletonList(user.getRole());
    }

    private User getUser(String principal) {
        if (principal.equals("kdemirov")) {
            return new User(principal, "test", "test@gmail.com", "Test", "Test", Role.ROLE_USER);
        } else {
            return new User(principal, "test", "test@gmail.com", "Test", "Test", Role.ROLE_ADMIN);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
