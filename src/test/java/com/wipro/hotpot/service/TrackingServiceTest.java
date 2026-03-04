package com.wipro.hotpot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wipro.hotpot.repository.*;
import com.wipro.hotpot.dto.OrderStatusDTO;
import com.wipro.hotpot.dto.TrackingDTO;
import com.wipro.hotpot.entity.Order;
import com.wipro.hotpot.entity.OrderTracking;
import com.wipro.hotpot.entity.Restaurant;
import com.wipro.hotpot.entity.User;

@ExtendWith(MockitoExtension.class)
public class TrackingServiceTest {

    @Mock
    private ITrackingRepository trackingRepository;

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private TrackingServiceImpl trackingService;

    private Order order;
    private OrderTracking tracking;
    private User user;
    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {

      
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@gmail.com");

       
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("HotPot Restaurant");

      
        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setStatus(Order.OrderStatus.PLACED);
        order.setTotalAmount(500.0);

     
        tracking = new OrderTracking();
        tracking.setId(1L);
        tracking.setOrder(order);
        tracking.setStatus(OrderTracking.TrackingStatus.PLACED);
        tracking.setMessage("Your order has been placed successfully!");
        tracking.setUpdatedAt(LocalDateTime.now());
    }

  
    @Test
    public void testCreateTracking_Success() {

       
        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));
        when(trackingRepository.save(any(OrderTracking.class)))
                .thenReturn(tracking);

      
        OrderTracking result = trackingService.createTracking(1L);

      
        assertNotNull(result);
        assertEquals(OrderTracking.TrackingStatus.PLACED, result.getStatus());
        assertEquals("Your order has been placed successfully!", result.getMessage());

        verify(trackingRepository, times(1)).save(any(OrderTracking.class));

        System.out.println("✅ Create Tracking Test Passed!");
    }

    
    @Test
    public void testCreateTracking_OrderNotFound() {

       
        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trackingService.createTracking(99L);
        });

        assertEquals("Order not found!", exception.getMessage());
        verify(trackingRepository, never()).save(any(OrderTracking.class));

        System.out.println("✅ Order Not Found Test Passed!");
    }

    
    @Test
    public void testUpdateOrderStatus_Success() {

       
        OrderStatusDTO dto = new OrderStatusDTO();
        dto.setOrderId(1L);
        dto.setStatus("DISPATCHED");
        dto.setMessage("Your order is out for delivery!");

        when(trackingRepository.findByOrderId(1L))
                .thenReturn(Optional.of(tracking));
        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(trackingRepository.save(any(OrderTracking.class)))
                .thenReturn(tracking);

     
        OrderTracking result = trackingService.updateOrderStatus(dto);

       
        assertNotNull(result);
        assertEquals(OrderTracking.TrackingStatus.DISPATCHED, tracking.getStatus());

      
        verify(emailService, times(1))
                .sendOrderStatusEmail(anyString(), anyString(), anyLong(), anyString());

        System.out.println("✅ Update Order Status Test Passed!");
    }

  
    @Test
    public void testGetTrackingDetails_Success() {

    
        when(trackingRepository.findByOrderId(1L))
                .thenReturn(Optional.of(tracking));

       
        TrackingDTO result = trackingService.getTrackingDetails(1L);

       
        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals("PLACED", result.getStatus());
        assertEquals("Your order has been placed successfully!", result.getMessage());

        System.out.println("✅ Get Tracking Details Test Passed!");
    }
}
