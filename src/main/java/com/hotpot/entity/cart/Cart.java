package com.hotpot.entity.cart;

import java.util.List;

import com.hotpot.entity.auth.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    private Double totalPrice = 0.0;

    public Cart() {}

    public Cart(Long id, User user, List<CartItem> cartItems, Double totalPrice) {
        this.id = id;
        this.user = user;
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public List<CartItem> getCartItems() { return cartItems; }
    public Double getTotalPrice() { return totalPrice; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    @Override
    public String toString() {
        return "Cart{id=" + id + ", totalPrice=" + totalPrice + "}";
    }
}