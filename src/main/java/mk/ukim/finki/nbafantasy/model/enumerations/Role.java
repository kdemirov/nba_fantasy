package mk.ukim.finki.nbafantasy.model.enumerations;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * User roles.
 */
public enum Role implements GrantedAuthority, Serializable {
    ROLE_ADMIN, ROLE_USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
