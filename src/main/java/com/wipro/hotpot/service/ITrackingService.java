package com.wipro.hotpot.service;

import com.wipro.hotpot.dto.TrackingDTO;

public interface ITrackingService {

   
    TrackingDTO getTrackingByOrderId(Long orderId);

    
    TrackingDTO updateTracking(Long orderId, String status, String message);

    TrackingDTO createTracking(Long orderId);
}