package com.hotpot.entity.menu;

import java.time.LocalDateTime;

import com.hotpot.entity.auth.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;
    private String contactNumber;
    private String description;
    private String imageUrl;

    @Column(nullable = false)
    private boolean isActive = true;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

   
    public Restaurant() {}

    
    public Restaurant(Long id, String name, String location, String contactNumber,
                      String description, String imageUrl, boolean isActive,
                      User owner, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.contactNumber = contactNumber;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.owner = owner;
        this.createdAt = createdAt;
    }

    // ✅ Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getContactNumber() { return contactNumber; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public boolean isActive() { return isActive; }
    public User getOwner() { return owner; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ✅ Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
    public void setOwner(User owner) { this.owner = owner; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Restaurant{id=" + id + ", name=" + name + ", location=" + location + "}";
    }
}
