package com.hotpot.service.tracking;

import com.hotpot.dto.tracking.TrackingDTO;
import com.hotpot.dto.tracking.OrderStatusDTO;
import com.hotpot.entity.tracking.OrderTracking;
import java.util.List;

public interface TrackingService {

	OrderTracking createTracking(Long orderId);

	OrderTracking updateOrderStatus(OrderStatusDTO dto);

	OrderTracking getTrackingByOrderId(Long orderId);

	List<OrderTracking> getTrackingsByUserId(Long userId);

	List<OrderTracking> getTrackingsByRestaurantId(Long restaurantId);

	TrackingDTO getTrackingDetails(Long orderId);
}
