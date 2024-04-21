package mk.ukim.finki.nbafantasy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Web resources configuration for templates, javascript, css files and images.
 */
@Configuration
public class WebResourceConfiguration extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**", "static/scripts/**", "static/images/**", "static/**")
                .addResourceLocations("classpath:templates/**", "classpath:static/scripts/**", "classpath:static/images/**", "classpath:static/");
    }
}
