package com.hotpot.entity.cart;

import com.hotpot.entity.menu.MenuItem;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    private Integer quantity;
    private Double totalItemPrice;

    public CartItem() {}

    public CartItem(Long id, Cart cart, MenuItem menuItem,
                    Integer quantity, Double totalItemPrice) {
        this.id = id;
        this.cart = cart;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.totalItemPrice = totalItemPrice;
    }

    public Long getId() { return id; }
    public Cart getCart() { return cart; }
    public MenuItem getMenuItem() { return menuItem; }
    public Integer getQuantity() { return quantity; }
    public Double getTotalItemPrice() { return totalItemPrice; }

    public void setId(Long id) { this.id = id; }
    public void setCart(Cart cart) { this.cart = cart; }
    public void setMenuItem(MenuItem menuItem) { this.menuItem = menuItem; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setTotalItemPrice(Double totalItemPrice) { this.totalItemPrice = totalItemPrice; }
}