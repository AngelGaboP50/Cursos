package com.example.demo.service;

import com.example.demo.dto.response.CourseDTO;
import com.example.demo.model.Course;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

	private final CourseRepository courseRepository;

	@Autowired
	public CourseService(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	public List<CourseDTO> getAllCourses() {
		List<Course> courses = courseRepository.findAll();
		return courses.stream()
				.map(c -> new CourseDTO(c.getId(), c.getTitle(), c.getDescription(), c.getLevel(), c.getPrice(), c.getPublished(), c.getImageUrl()))
				.collect(Collectors.toList());
	}

}
