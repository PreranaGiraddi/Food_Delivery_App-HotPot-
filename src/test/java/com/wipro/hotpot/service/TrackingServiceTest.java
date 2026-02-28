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

        // Sample user
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@gmail.com");

        // Sample restaurant
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("HotPot Restaurant");

        // Sample order
        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setStatus(Order.OrderStatus.PLACED);
        order.setTotalAmount(500.0);

        // Sample tracking
        tracking = new OrderTracking();
        tracking.setId(1L);
        tracking.setOrder(order);
        tracking.setStatus(OrderTracking.TrackingStatus.PLACED);
        tracking.setMessage("Your order has been placed successfully!");
        tracking.setUpdatedAt(LocalDateTime.now());
    }

    // ✅ TEST 1 — Create tracking when order is placed
    @Test
    public void testCreateTracking_Success() {

        // ARRANGE
        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));
        when(trackingRepository.save(any(OrderTracking.class)))
                .thenReturn(tracking);

        // ACT
        OrderTracking result = trackingService.createTracking(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(OrderTracking.TrackingStatus.PLACED, result.getStatus());
        assertEquals("Your order has been placed successfully!", result.getMessage());

        verify(trackingRepository, times(1)).save(any(OrderTracking.class));

        System.out.println("✅ Create Tracking Test Passed!");
    }

    // ✅ TEST 2 — Create tracking fails if order not found
    @Test
    public void testCreateTracking_OrderNotFound() {

        // ARRANGE
        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trackingService.createTracking(99L);
        });

        assertEquals("Order not found!", exception.getMessage());
        verify(trackingRepository, never()).save(any(OrderTracking.class));

        System.out.println("✅ Order Not Found Test Passed!");
    }

    // ✅ TEST 3 — Update order status successfully
    @Test
    public void testUpdateOrderStatus_Success() {

        // ARRANGE
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

        // ACT
        OrderTracking result = trackingService.updateOrderStatus(dto);

        // ASSERT
        assertNotNull(result);
        assertEquals(OrderTracking.TrackingStatus.DISPATCHED, tracking.getStatus());

        // Verify email was sent
        verify(emailService, times(1))
                .sendOrderStatusEmail(anyString(), anyString(), anyLong(), anyString());

        System.out.println("✅ Update Order Status Test Passed!");
    }

    // ✅ TEST 4 — Get tracking details as DTO
    @Test
    public void testGetTrackingDetails_Success() {

        // ARRANGE
        when(trackingRepository.findByOrderId(1L))
                .thenReturn(Optional.of(tracking));

        // ACT
        TrackingDTO result = trackingService.getTrackingDetails(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals("PLACED", result.getStatus());
        assertEquals("Your order has been placed successfully!", result.getMessage());

        System.out.println("✅ Get Tracking Details Test Passed!");
    }
}
