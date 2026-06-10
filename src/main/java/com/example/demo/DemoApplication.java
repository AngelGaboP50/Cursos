package com.example.demo;

import com.example.demo.model.Course;
import com.example.demo.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(CourseRepository courseRepository) {
		return args -> {
			if (courseRepository.count() == 0) {
				courseRepository.save(new Course(null, "Java Básico", "Fundamentos de Java para principiantes", "Beginner", 0.0, true, null));
				courseRepository.save(new Course(null, "Spring Boot API", "Construye APIs REST con Spring Boot", "Intermediate", 49.99, true, null));
				courseRepository.save(new Course(null, "Desarrollo Web", "HTML, CSS y JavaScript para aplicaciones modernas", "Beginner", 19.99, true, null));
			}
		};
	}

}
