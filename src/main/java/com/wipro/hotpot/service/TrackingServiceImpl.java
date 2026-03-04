package com.wipro.hotpot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.hotpot.dto.OrderStatusDTO;
import com.wipro.hotpot.dto.TrackingDTO;
import com.wipro.hotpot.entity.Order;
import com.wipro.hotpot.entity.OrderTracking;
import com.wipro.hotpot.exception.ResourceNotFoundException;
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

	
	@Override
	public OrderTracking createTracking(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found!"));

		OrderTracking tracking = new OrderTracking();
		tracking.setOrder(order);
		tracking.setStatus(OrderTracking.TrackingStatus.PLACED);
		tracking.setMessage("Your order has been placed successfully!");

		return trackingRepository.save(tracking);
	}

	
	@Override
	public OrderTracking updateOrderStatus(OrderStatusDTO dto) {

		OrderTracking tracking = trackingRepository.findByOrderId(dto.getOrderId())
				.orElseThrow(() -> new ResourceNotFoundException("Tracking not found!"));

	
		tracking.setStatus(OrderTracking.TrackingStatus.valueOf(dto.getStatus()));
		tracking.setMessage(dto.getMessage());

	
		Order order = tracking.getOrder();
		order.setStatus(Order.OrderStatus.valueOf(dto.getStatus()));
		orderRepository.save(order);

		OrderTracking updated = trackingRepository.save(tracking);

		
		emailService.sendOrderStatusEmail(order.getUser().getEmail(), order.getUser().getName(), order.getId(),
				dto.getStatus());

		return updated;
	}


	@Override
	public OrderTracking getTrackingByOrderId(Long orderId) {
		return trackingRepository.findByOrderId(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Tracking not found for order: " + orderId));

	}

	
	@Override
	public List<OrderTracking> getTrackingsByUserId(Long userId) {
		return trackingRepository.findAllByUserId(userId);
	}


	@Override
	public List<OrderTracking> getTrackingsByRestaurantId(Long restaurantId) {
		return trackingRepository.findAllByRestaurantId(restaurantId);
	}

	
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
