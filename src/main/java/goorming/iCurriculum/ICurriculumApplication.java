package goorming.iCurriculum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ICurriculumApplication {
	public static void main(String[] args) {
		SpringApplication.run(ICurriculumApplication.class, args);

	}

}
