package mk.ukim.finki.nbafantasy;

import mk.ukim.finki.nbafantasy.config.NbaFantasyUrlAuthenticationSuccessHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@SpringBootApplication
@ServletComponentScan
public class NbaFantasyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NbaFantasyApplication.class, args);
    }

    @Bean
    public WebDriver getDriver(){
        System.setProperty("webdriver.gecko.driver","C:\\Users\\kdemirov\\nba_fantasy-master\\src\\main\\resources\\geckodriver-v0.33.0-win64\\geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions()
                .addPreference("browser.startup.page", 1)
                .addPreference("browser.startup.homepage", "https://www.google.com")
                .setAcceptInsecureCerts(true)
                .setHeadless(true);
        return new FirefoxDriver(options);
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
