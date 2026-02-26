package com.hotpot.entity.tracking;

import com.hotpot.entity.cart.Order;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_tracking")
public class OrderTracking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Enumerated(EnumType.STRING)
	private TrackingStatus status;

	private String message;

	private LocalDateTime updatedAt;

	@PrePersist
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public enum TrackingStatus {
		PLACED, CONFIRMED, PROCESSING, DISPATCHED, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
	}

	public OrderTracking() {
	}

	public OrderTracking(Long id, Order order, TrackingStatus status, String message, LocalDateTime updatedAt) {
		this.id = id;
		this.order = order;
		this.status = status;
		this.message = message;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public Order getOrder() {
		return order;
	}

	public TrackingStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setStatus(TrackingStatus status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "OrderTracking{id=" + id + ", status=" + status + "}";
	}
}
