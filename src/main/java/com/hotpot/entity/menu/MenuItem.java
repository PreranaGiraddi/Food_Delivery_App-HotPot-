package com.hotpot.entity.menu;

import jakarta.persistence.*;

@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    private Double discountPrice;
    private String imageUrl;
    private String availabilityTime;

    @Enumerated(EnumType.STRING)
    private DietaryType dietaryType;

    private String tasteInfo;
    private String nutritionalInfo;
    private Integer cookingTime;
    private boolean isAvailable = true;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public enum DietaryType {
        VEG, NONVEG
    }

    // ✅ No-arg Constructor
    public MenuItem() {}

    // ✅ All-arg Constructor
    public MenuItem(Long id, String name, String description, Double price,
                    Double discountPrice, String imageUrl, String availabilityTime,
                    DietaryType dietaryType, String tasteInfo, String nutritionalInfo,
                    Integer cookingTime, boolean isAvailable,
                    Category category, Restaurant restaurant) {
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
        this.category = category;
        this.restaurant = restaurant;
    }

    // ✅ Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public Double getDiscountPrice() { return discountPrice; }
    public String getImageUrl() { return imageUrl; }
    public String getAvailabilityTime() { return availabilityTime; }
    public DietaryType getDietaryType() { return dietaryType; }
    public String getTasteInfo() { return tasteInfo; }
    public String getNutritionalInfo() { return nutritionalInfo; }
    public Integer getCookingTime() { return cookingTime; }
    public boolean isAvailable() { return isAvailable; }
    public Category getCategory() { return category; }
    public Restaurant getRestaurant() { return restaurant; }

    // ✅ Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setDiscountPrice(Double discountPrice) { this.discountPrice = discountPrice; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setAvailabilityTime(String availabilityTime) { this.availabilityTime = availabilityTime; }
    public void setDietaryType(DietaryType dietaryType) { this.dietaryType = dietaryType; }
    public void setTasteInfo(String tasteInfo) { this.tasteInfo = tasteInfo; }
    public void setNutritionalInfo(String nutritionalInfo) { this.nutritionalInfo = nutritionalInfo; }
    public void setCookingTime(Integer cookingTime) { this.cookingTime = cookingTime; }
    public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }
    public void setCategory(Category category) { this.category = category; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    @Override
    public String toString() {
        return "MenuItem{id=" + id + ", name=" + name + ", price=" + price + "}";
    }
}
