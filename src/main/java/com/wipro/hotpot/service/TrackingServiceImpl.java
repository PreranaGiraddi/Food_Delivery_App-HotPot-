package com.wipro.hotpot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.hotpot.dto.OrderStatusDTO;
import com.wipro.hotpot.dto.TrackingDTO;
import com.wipro.hotpot.entity.Order;
import com.wipro.hotpot.entity.OrderTracking;
import com.wipro.hotpot.repository.IOrderRepository;
import com.wipro.hotpot.repository.ITrackingRepository;

@Service
public class TrackingServiceImpl implements ITrackingService {

	@Autowired
	private ITrackingRepository trackingRepository;

	@Autowired
	private IOrderRepository orderRepository;

	@Autowired
	private EmailService emailService;

	// ✅ Create tracking when order is placed
	@Override
	public OrderTracking createTracking(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found!"));

		OrderTracking tracking = new OrderTracking();
		tracking.setOrder(order);
		tracking.setStatus(OrderTracking.TrackingStatus.PLACED);
		tracking.setMessage("Your order has been placed successfully!");

		return trackingRepository.save(tracking);
	}

	// ✅ Update order status
	@Override
	public OrderTracking updateOrderStatus(OrderStatusDTO dto) {

		OrderTracking tracking = trackingRepository.findByOrderId(dto.getOrderId())
				.orElseThrow(() -> new RuntimeException("Tracking not found!"));

		// Update tracking status
		tracking.setStatus(OrderTracking.TrackingStatus.valueOf(dto.getStatus()));
		tracking.setMessage(dto.getMessage());

		// Also update order status
		Order order = tracking.getOrder();
		order.setStatus(Order.OrderStatus.valueOf(dto.getStatus()));
		orderRepository.save(order);

		OrderTracking updated = trackingRepository.save(tracking);

		// Send email notification to user
		emailService.sendOrderStatusEmail(order.getUser().getEmail(), order.getUser().getName(), order.getId(),
				dto.getStatus());

		return updated;
	}

	// ✅ Get tracking by order id
	@Override
	public OrderTracking getTrackingByOrderId(Long orderId) {
		return trackingRepository.findByOrderId(orderId)
				.orElseThrow(() -> new RuntimeException("Tracking not found for order: " + orderId));
	}

	// ✅ Get all trackings by user
	@Override
	public List<OrderTracking> getTrackingsByUserId(Long userId) {
		return trackingRepository.findAllByUserId(userId);
	}

	// ✅ Get all trackings by restaurant
	@Override
	public List<OrderTracking> getTrackingsByRestaurantId(Long restaurantId) {
		return trackingRepository.findAllByRestaurantId(restaurantId);
	}

	// ✅ Get tracking as DTO
	@Override
	public TrackingDTO getTrackingDetails(Long orderId) {
		OrderTracking tracking = getTrackingByOrderId(orderId);

		TrackingDTO dto = new TrackingDTO();
		dto.setTrackingId(tracking.getId());
		dto.setOrderId(orderId);
		dto.setStatus(tracking.getStatus().name());
		dto.setMessage(tracking.getMessage());
		dto.setUpdatedAt(tracking.getUpdatedAt());

		return dto;
	}
}
