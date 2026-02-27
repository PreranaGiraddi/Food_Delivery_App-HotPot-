package com.wipro.hotpot.service;

import java.util.List;

import com.wipro.hotpot.dto.OrderStatusDTO;
import com.wipro.hotpot.dto.TrackingDTO;
import com.wipro.hotpot.entity.OrderTracking;

public interface ITrackingService {

	OrderTracking createTracking(Long orderId);

	OrderTracking updateOrderStatus(OrderStatusDTO dto);

	OrderTracking getTrackingByOrderId(Long orderId);

	List<OrderTracking> getTrackingsByUserId(Long userId);

	List<OrderTracking> getTrackingsByRestaurantId(Long restaurantId);

	TrackingDTO getTrackingDetails(Long orderId);
}
