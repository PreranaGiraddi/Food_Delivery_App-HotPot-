package com.wipro.hotpot.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	private Double totalAmount;
	private String deliveryAddress;
	private String paymentMethod;

	@Column(updatable = false)
	private LocalDateTime orderedAt;

	@PrePersist
	public void prePersist() {
		this.orderedAt = LocalDateTime.now();
		this.status = OrderStatus.PLACED;
	}

	public enum OrderStatus {
		PLACED, CONFIRMED, PROCESSING, DISPATCHED, DELIVERED, CANCELLED
	}

	public Order() {
	}

	public Order(Long id, User user, Restaurant restaurant, List<OrderItem> orderItems, OrderStatus status,
			Double totalAmount, String deliveryAddress, String paymentMethod, LocalDateTime orderedAt) {
		this.id = id;
		this.user = user;
		this.restaurant = restaurant;
		this.orderItems = orderItems;
		this.status = status;
		this.totalAmount = totalAmount;
		this.deliveryAddress = deliveryAddress;
		this.paymentMethod = paymentMethod;
		this.orderedAt = orderedAt;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public LocalDateTime getOrderedAt() {
		return orderedAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public void setOrderedAt(LocalDateTime orderedAt) {
		this.orderedAt = orderedAt;
	}

	@Override
	public String toString() {
		return "Order{id=" + id + ", status=" + status + ", totalAmount=" + totalAmount + "}";
	}
}