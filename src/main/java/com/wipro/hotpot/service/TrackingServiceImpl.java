package com.wipro.hotpot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private String getDefaultMessage(String status) {
        return switch (status.toUpperCase()) {
            case "PLACED"           -> "Your order has been placed successfully!";
            case "CONFIRMED"        -> "Restaurant has confirmed your order!";
            case "PROCESSING"       -> "Your food is being prepared!";
            case "DISPATCHED"       -> "Your order has been dispatched!";
            case "OUT_FOR_DELIVERY" -> "Your order is out for delivery!";
            case "DELIVERED"        -> "Your order has been delivered. Enjoy your meal!";
            case "CANCELLED"        -> "Your order has been cancelled.";
            default                 -> "Order status updated to " + status;
        };
    }

    
    @Override
    public TrackingDTO getTrackingByOrderId(Long orderId) {
        Optional<OrderTracking> opt = trackingRepository.findByOrderId(orderId);

        if (opt.isEmpty()) {
            
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

           
            OrderTracking tracking = new OrderTracking();
            tracking.setOrder(order);
            tracking.setStatus(OrderTracking.TrackingStatus.PLACED);
            tracking.setMessage("Your order has been placed successfully!");
            trackingRepository.save(tracking);
            return toDTO(tracking);
        }

        return toDTO(opt.get());
    }

    
    @Override
    public TrackingDTO updateTracking(Long orderId, String status, String message) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

       
        OrderTracking.TrackingStatus trackingStatus;
        try {
            trackingStatus = OrderTracking.TrackingStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
           
            trackingStatus = mapOrderStatusToTrackingStatus(status);
        }

        
        String finalMessage = (message != null && !message.isBlank())
                ? message
                : getDefaultMessage(status);

        
        Optional<OrderTracking> opt = trackingRepository.findByOrderId(orderId);
        OrderTracking tracking;

        if (opt.isPresent()) {
            tracking = opt.get();
        } else {
            tracking = new OrderTracking();
            tracking.setOrder(order);
        }

        tracking.setStatus(trackingStatus);
        tracking.setMessage(finalMessage);
        trackingRepository.save(tracking);

        return toDTO(tracking);
    }

   
    @Override
    public TrackingDTO createTracking(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

     
        Optional<OrderTracking> existing = trackingRepository.findByOrderId(orderId);
        if (existing.isPresent()) {
            return toDTO(existing.get());
        }

        OrderTracking tracking = new OrderTracking();
        tracking.setOrder(order);
        tracking.setStatus(OrderTracking.TrackingStatus.PLACED);
        tracking.setMessage("Your order has been placed successfully!");
        trackingRepository.save(tracking);

        return toDTO(tracking);
    }

  
    private OrderTracking.TrackingStatus mapOrderStatusToTrackingStatus(String orderStatus) {
        return switch (orderStatus.toUpperCase()) {
            case "PLACED"           -> OrderTracking.TrackingStatus.PLACED;
            case "CONFIRMED"        -> OrderTracking.TrackingStatus.CONFIRMED;
            case "PROCESSING"       -> OrderTracking.TrackingStatus.PROCESSING;
            case "DISPATCHED"       -> OrderTracking.TrackingStatus.DISPATCHED;
            case "OUT_FOR_DELIVERY" -> OrderTracking.TrackingStatus.OUT_FOR_DELIVERY;
            case "DELIVERED"        -> OrderTracking.TrackingStatus.DELIVERED;
            case "CANCELLED"        -> OrderTracking.TrackingStatus.CANCELLED;
            default -> throw new RuntimeException("Unknown status: " + orderStatus);
        };
    }

    
    private TrackingDTO toDTO(OrderTracking t) {
        TrackingDTO dto = new TrackingDTO();
        dto.setTrackingId(t.getId());
        dto.setOrderId(t.getOrder().getId());
        dto.setStatus(t.getStatus() != null ? t.getStatus().name() : "PLACED");
        dto.setMessage(t.getMessage());
        dto.setUpdatedAt(t.getUpdatedAt());
        return dto;
    }
}