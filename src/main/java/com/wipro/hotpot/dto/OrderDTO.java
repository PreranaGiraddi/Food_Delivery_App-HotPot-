package com.wipro.hotpot.dto;

import jakarta.validation.constraints.*;

public class OrderDTO {

	private Long orderId;
	private Long userId;
	private Long restaurantId;

	@NotBlank(message = "Delivery address is required")
	private String deliveryAddress;

	@NotBlank(message = "Payment method is required")
	private String paymentMethod;

	private Double totalAmount;
	private String status;

	public OrderDTO() {
	}

	public OrderDTO(Long orderId, Long userId, Long restaurantId, String deliveryAddress, String paymentMethod,
			Double totalAmount, String status) {
		this.orderId = orderId;
		this.userId = userId;
		this.restaurantId = restaurantId;
		this.deliveryAddress = deliveryAddress;
		this.paymentMethod = paymentMethod;
		this.totalAmount = totalAmount;
		this.status = status;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}