package com.wipro.hotpot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.hotpot.dto.TrackingDTO;
import com.wipro.hotpot.service.ITrackingService;

@RestController
@RequestMapping("/api/tracking")
@CrossOrigin(origins = "*")
public class TrackingController {

    @Autowired
    private ITrackingService trackingService;

    // ─── GET tracking by order ID ─────────────────────────────────────────────
    // Called by user dashboard: GET /api/tracking/order/42
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getTrackingByOrder(@PathVariable Long orderId) {
        try {
            TrackingDTO tracking = trackingService.getTrackingByOrderId(orderId);
            return ResponseEntity.ok(tracking);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Tracking not found: " + e.getMessage());
        }
    }

    // ─── UPDATE tracking status ───────────────────────────────────────────────
    // Called by restaurant dashboard when updating order status
    // PUT /api/tracking/update/42?status=CONFIRMED
    // PUT /api/tracking/update/42?status=CONFIRMED&message=Custom message
    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateTracking(
            @PathVariable Long orderId,
            @RequestParam String status,
            @RequestParam(required = false) String message) {
        try {
            TrackingDTO updated = trackingService.updateTracking(orderId, status, message);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update tracking: " + e.getMessage());
        }
    }

    // ─── CREATE initial tracking ──────────────────────────────────────────────
    // Called after placing order if auto-creation fails
    // POST /api/tracking/create/42
    @PostMapping("/create/{orderId}")
    public ResponseEntity<?> createTracking(@PathVariable Long orderId) {
        try {
            TrackingDTO tracking = trackingService.createTracking(orderId);
            return ResponseEntity.ok(tracking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create tracking: " + e.getMessage());
        }
    }
}