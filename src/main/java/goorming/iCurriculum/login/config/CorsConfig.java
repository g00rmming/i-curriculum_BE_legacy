package goorming.iCurriculum.login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")

                .allowedOrigins("https://d3rus1qyc0lc4p.cloudfront.net/")
                .allowedOrigins("http://localhost:8082")
                .allowedOrigins("http://13.214.35.243:8082")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);

    }
}
