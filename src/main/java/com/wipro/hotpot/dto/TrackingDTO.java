package com.wipro.hotpot.dto;

import java.time.LocalDateTime;

public class TrackingDTO {

	private Long trackingId;
	private Long orderId;
	private String status;
	private String message;
	private LocalDateTime updatedAt;

	public TrackingDTO() {
	}

	public TrackingDTO(Long trackingId, Long orderId, String status, String message, LocalDateTime updatedAt) {
		this.trackingId = trackingId;
		this.orderId = orderId;
		this.status = status;
		this.message = message;
		this.updatedAt = updatedAt;
	}

	public Long getTrackingId() {
		return trackingId;
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

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setTrackingId(Long trackingId) {
		this.trackingId = trackingId;
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

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
