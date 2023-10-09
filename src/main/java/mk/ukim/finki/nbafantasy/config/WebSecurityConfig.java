package mk.ukim.finki.nbafantasy.config;

import mk.ukim.finki.nbafantasy.service.UserService;
import mk.ukim.finki.nbafantasy.web.filters.MyTeamFilter;
import mk.ukim.finki.nbafantasy.web.filters.ValidationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUsernamePasswordAuthenticationProvider authenticationProvider;
    private final AuthenticationSuccessHandler successHandler;
    private final UserService userService;

    public WebSecurityConfig(CustomUsernamePasswordAuthenticationProvider authenticationProvider, AuthenticationSuccessHandler successHandler, UserService userService) {
        this.authenticationProvider = authenticationProvider;
        this.successHandler = successHandler;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/home","/register","/assets/**","/images/**","/style.css").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .failureUrl("/login?error=Invalid username or password")
                .successHandler(successHandler)
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login")
                .and()
                .exceptionHandling().accessDeniedPage("/access_denied")
                .and()
                .addFilterAfter(new ValidationFilter(userService),UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new MyTeamFilter(userService),ValidationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}
