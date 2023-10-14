package mk.ukim.finki.nbafantasy;

import mk.ukim.finki.nbafantasy.config.NbaFantasyUrlAuthenticationSuccessHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@SpringBootApplication
public class NbaFantasyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NbaFantasyApplication.class, args);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new NbaFantasyUrlAuthenticationSuccessHandler();
    }
}
