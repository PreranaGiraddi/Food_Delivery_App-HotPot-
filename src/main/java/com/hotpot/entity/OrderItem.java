package com.hotpot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "menu_item_id")
	private MenuItem menuItem;

	private Integer quantity;
	private Double price;

	public OrderItem() {
	}

	public OrderItem(Long id, Order order, MenuItem menuItem, Integer quantity, Double price) {
		this.id = id;
		this.order = order;
		this.menuItem = menuItem;
		this.quantity = quantity;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public Order getOrder() {
		return order;
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}