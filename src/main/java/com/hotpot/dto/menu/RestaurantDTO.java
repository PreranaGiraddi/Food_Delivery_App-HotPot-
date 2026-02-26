package com.hotpot.dto.menu;

import jakarta.validation.constraints.*;

public class RestaurantDTO {

    private Long id;

    @NotBlank(message = "Restaurant name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    private String contactNumber;
    private String description;
    private String imageUrl;

    public RestaurantDTO() {}

    public RestaurantDTO(Long id, String name, String location,
                         String contactNumber, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.contactNumber = contactNumber;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getContactNumber() { return contactNumber; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
