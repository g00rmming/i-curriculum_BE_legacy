package goorming.iCurriculum.login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")

                .allowedOrigins("http://grooming-01-s3.s3-website-ap-southeast-1.amazonaws.com/")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);

    }
}
