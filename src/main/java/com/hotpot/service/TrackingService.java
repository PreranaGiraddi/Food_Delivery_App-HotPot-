package com.hotpot.service;

import java.util.List;

import com.hotpot.dto.OrderStatusDTO;
import com.hotpot.dto.TrackingDTO;
import com.hotpot.entity.OrderTracking;

public interface TrackingService {

	OrderTracking createTracking(Long orderId);

	OrderTracking updateOrderStatus(OrderStatusDTO dto);

	OrderTracking getTrackingByOrderId(Long orderId);

	List<OrderTracking> getTrackingsByUserId(Long userId);

	List<OrderTracking> getTrackingsByRestaurantId(Long restaurantId);

	TrackingDTO getTrackingDetails(Long orderId);
}
