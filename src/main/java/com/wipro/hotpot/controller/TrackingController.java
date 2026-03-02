package com.wipro.hotpot.controller;

import com.wipro.hotpot.dto.OrderStatusDTO;
import com.wipro.hotpot.dto.TrackingDTO;
import com.wipro.hotpot.entity.OrderTracking;
import com.wipro.hotpot.service.ITrackingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

	@Autowired
	private ITrackingService trackingService;

	// Create tracking when order is placed
	// Input : orderId (path variable)
	// Return : OrderTracking object
	@PostMapping("/create/{orderId}")
	public ResponseEntity<OrderTracking> createTracking(@PathVariable Long orderId) {
		OrderTracking tracking = trackingService.createTracking(orderId);
		return new ResponseEntity<>(tracking, HttpStatus.CREATED);
	}

	// Update order status
	// Input : OrderStatusDTO (orderId, status, message)
	// Return : Updated OrderTracking object
	@PutMapping("/update")
	public ResponseEntity<OrderTracking> updateOrderStatus(@Valid @RequestBody OrderStatusDTO dto) {
		OrderTracking tracking = trackingService.updateOrderStatus(dto);
		return new ResponseEntity<>(tracking, HttpStatus.OK);
	}

	// Get tracking by order id
	// Input : orderId (path variable)
	// Return : OrderTracking object
	@GetMapping("/order/{orderId}")
	public ResponseEntity<OrderTracking> getTrackingByOrderId(@PathVariable Long orderId) {
		OrderTracking tracking = trackingService.getTrackingByOrderId(orderId);
		return new ResponseEntity<>(tracking, HttpStatus.OK);
	}

	// Get all trackings by user id
	// Input : userId (path variable)
	// Return : List of OrderTracking objects
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<OrderTracking>> getTrackingsByUserId(@PathVariable Long userId) {
		List<OrderTracking> trackings = trackingService.getTrackingsByUserId(userId);
		return new ResponseEntity<>(trackings, HttpStatus.OK);
	}

	// Get all trackings by restaurant id
	// Input : restaurantId (path variable)
	// Return : List of OrderTracking objects
	@GetMapping("/restaurant/{restaurantId}")
	public ResponseEntity<List<OrderTracking>> getTrackingsByRestaurantId(@PathVariable Long restaurantId) {
		List<OrderTracking> trackings = trackingService.getTrackingsByRestaurantId(restaurantId);
		return new ResponseEntity<>(trackings, HttpStatus.OK);
	}

	// Get tracking details as DTO
	// Input : orderId (path variable)
	// Return : TrackingDTO object
	@GetMapping("/details/{orderId}")
	public ResponseEntity<TrackingDTO> getTrackingDetails(@PathVariable Long orderId) {
		TrackingDTO dto = trackingService.getTrackingDetails(orderId);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
}
