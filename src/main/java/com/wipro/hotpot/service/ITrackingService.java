package com.wipro.hotpot.service;

import com.wipro.hotpot.dto.TrackingDTO;

public interface ITrackingService {

    // Get tracking by order ID
    TrackingDTO getTrackingByOrderId(Long orderId);

    // Update tracking status (called when restaurant updates order status)
    TrackingDTO updateTracking(Long orderId, String status, String message);

    // Create initial tracking record for an order
    TrackingDTO createTracking(Long orderId);
}