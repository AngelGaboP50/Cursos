package com.example.demo.dto.response;

public class CourseDTO {

	private Long id;
	private String title;
	private String description;
	private String level;
	private Double price;
	private Boolean published;
	private String imageUrl;

	public CourseDTO() {
	}

	public CourseDTO(Long id, String title, String description, String level, Double price, Boolean published, String imageUrl) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.level = level;
		this.price = price;
		this.published = published;
		this.imageUrl = imageUrl;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getLevel() {
		return level;
	}

	public Double getPrice() {
		return price;
	}

	public Boolean getPublished() {
		return published;
	}

	public String getImageUrl() {
		return imageUrl;
	}

}
