package com.wipro.hotpot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonIgnoreProperties({"owner", "menuItems", "categories"})
    private Restaurant restaurant;

    // ── Constructors ──
    public Category() {}

    public Category(String name, Restaurant restaurant) {
        this.name = name;
        this.restaurant = restaurant;
    }

    // ── Getters ──
    public Long getId()              { return id; }
    public String getName()          { return name; }
    public Restaurant getRestaurant(){ return restaurant; }

    // ── Setters ──
    public void setId(Long id)                      { this.id = id; }
    public void setName(String name)                { this.name = name; }
    public void setRestaurant(Restaurant restaurant){ this.restaurant = restaurant; }

    @Override
    public String toString() {
        return "Category{id=" + id + ", name=" + name + "}";
    }
}