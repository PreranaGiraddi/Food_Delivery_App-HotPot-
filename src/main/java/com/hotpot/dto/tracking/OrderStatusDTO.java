package com.hotpot.dto.tracking;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.NotBlank;

public class OrderStatusDTO {

	@NotNull(message = "Order ID is required")
	private Long orderId;

	@NotBlank(message = "Status is required")
	private String status;

	private String message;

	public OrderStatusDTO() {
	}

	public OrderStatusDTO(Long orderId, String status, String message) {
		this.orderId = orderId;
		this.status = status;
		this.message = message;
	}

	public Long getOrderId() {
		return orderId;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
