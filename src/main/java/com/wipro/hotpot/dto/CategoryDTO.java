package com.wipro.hotpot.dto;

public class CategoryDTO {

    private String name;
    private Long restaurantId;

    
    public CategoryDTO() {}

    public CategoryDTO(String name, Long restaurantId) {
        this.name = name;
        this.restaurantId = restaurantId;
    }

   
    public String getName()          { return name; }
    public Long getRestaurantId()    { return restaurantId; }

    
    public void setName(String name)             { this.name = name; }
    public void setRestaurantId(Long restaurantId){ this.restaurantId = restaurantId; }
}