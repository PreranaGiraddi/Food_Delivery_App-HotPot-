package com.wipro.hotpot.dto;

import jakarta.validation.constraints.*;

public class MenuDTO {

	private Long id;

	@NotBlank(message = "Item name is required")
	private String name;

	private String description;

	@NotNull(message = "Price is required")
	@Min(value = 0, message = "Price cannot be negative")
	private Double price;

	private Double discountPrice;
	private String imageUrl;
	private String availabilityTime;
	private String dietaryType;
	private String tasteInfo;
	private String nutritionalInfo;
	private Integer cookingTime;
	private boolean isAvailable;
	private Long categoryId;
	private Long restaurantId;

	public MenuDTO() {
	}

	public MenuDTO(Long id, String name, String description, Double price, Double discountPrice, String imageUrl,
			String availabilityTime, String dietaryType, String tasteInfo, String nutritionalInfo, Integer cookingTime,
			boolean isAvailable, Long categoryId, Long restaurantId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.discountPrice = discountPrice;
		this.imageUrl = imageUrl;
		this.availabilityTime = availabilityTime;
		this.dietaryType = dietaryType;
		this.tasteInfo = tasteInfo;
		this.nutritionalInfo = nutritionalInfo;
		this.cookingTime = cookingTime;
		this.isAvailable = isAvailable;
		this.categoryId = categoryId;
		this.restaurantId = restaurantId;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Double getPrice() {
		return price;
	}

	public Double getDiscountPrice() {
		return discountPrice;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getAvailabilityTime() {
		return availabilityTime;
	}

	public String getDietaryType() {
		return dietaryType;
	}

	public String getTasteInfo() {
		return tasteInfo;
	}

	public String getNutritionalInfo() {
		return nutritionalInfo;
	}

	public Integer getCookingTime() {
		return cookingTime;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setAvailabilityTime(String availabilityTime) {
		this.availabilityTime = availabilityTime;
	}

	public void setDietaryType(String dietaryType) {
		this.dietaryType = dietaryType;
	}

	public void setTasteInfo(String tasteInfo) {
		this.tasteInfo = tasteInfo;
	}

	public void setNutritionalInfo(String nutritionalInfo) {
		this.nutritionalInfo = nutritionalInfo;
	}

	public void setCookingTime(Integer cookingTime) {
		this.cookingTime = cookingTime;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}
}