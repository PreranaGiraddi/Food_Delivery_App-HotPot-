package com.wipro.hotpot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

import com.wipro.hotpot.dto.TrackingDTO;
import com.wipro.hotpot.entity.Order;
import com.wipro.hotpot.entity.OrderTracking;
import com.wipro.hotpot.entity.Restaurant;
import com.wipro.hotpot.entity.User;
import com.wipro.hotpot.repository.IOrderRepository;
import com.wipro.hotpot.repository.ITrackingRepository;

@ExtendWith(MockitoExtension.class)
public class TrackingServiceTest {

    

    @Mock
    private ITrackingRepository trackingRepository;

    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private TrackingServiceImpl trackingService;

    // ─── Test Data ───────────────────────────────────────────────────────────

    private Order order;
    private OrderTracking tracking;
    private User user;
    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {

        user = new User();
        user.setId(1L);
        user.setName("Thushara S");
        user.setEmail("thusharasatheesh1@gmail.com");

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

    // =========================================================================
    // CREATE TRACKING TESTS
    // =========================================================================

    /**
     * Happy path: order exists, no tracking yet → creates and saves new PLACED tracking.
     */
    @Test
    public void testCreateTracking_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        // No existing tracking record
        when(trackingRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(trackingRepository.save(any(OrderTracking.class))).thenReturn(tracking);

        TrackingDTO result = trackingService.createTracking(1L);

        assertNotNull(result);
        // TrackingDTO.getStatus() returns String, not enum
        assertEquals("PLACED", result.getStatus());
        assertEquals("Your order has been placed successfully!", result.getMessage());
        assertEquals(1L, result.getOrderId());

        verify(trackingRepository, times(1)).save(any(OrderTracking.class));
        System.out.println("✅ testCreateTracking_Success passed");
    }

    /**
     * Tracking already exists for this order → returns existing record, no duplicate save.
     */
    @Test
    public void testCreateTracking_AlreadyExists_ReturnsExisting() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(trackingRepository.findByOrderId(1L)).thenReturn(Optional.of(tracking));

        TrackingDTO result = trackingService.createTracking(1L);

        assertNotNull(result);
        assertEquals("PLACED", result.getStatus());
        // Should NOT save a duplicate
        verify(trackingRepository, never()).save(any(OrderTracking.class));
        System.out.println("✅ testCreateTracking_AlreadyExists_ReturnsExisting passed");
    }

    /**
     * Order not found → RuntimeException, tracking never saved.
     * Exact message in service: "Order not found: " + orderId
     */
    @Test
    public void testCreateTracking_OrderNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            trackingService.createTracking(99L)
        );

        assertTrue(ex.getMessage().contains("Order not found"),
            "Expected 'Order not found' but got: " + ex.getMessage());
        verify(trackingRepository, never()).save(any(OrderTracking.class));
        System.out.println("✅ testCreateTracking_OrderNotFound passed");
    }

    // =========================================================================
    // GET TRACKING BY ORDER ID TESTS
    // =========================================================================

    /**
     * Tracking record exists → returned directly as DTO.
     * TrackingDTO.getStatus() is a String (not enum) — check with assertEquals("PLACED", ...)
     */
    @Test
    public void testGetTrackingByOrderId_TrackingExists() {
        when(trackingRepository.findByOrderId(1L)).thenReturn(Optional.of(tracking));

        TrackingDTO result = trackingService.getTrackingByOrderId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals("PLACED", result.getStatus());      // ✅ String, not enum
        assertEquals("Your order has been placed successfully!", result.getMessage());
        System.out.println("✅ testGetTrackingByOrderId_TrackingExists passed");
    }

    
    @Test
    public void testGetTrackingByOrderId_NoTracking_AutoCreates() {
        when(trackingRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(trackingRepository.save(any(OrderTracking.class))).thenReturn(tracking);

        TrackingDTO result = trackingService.getTrackingByOrderId(1L);

        assertNotNull(result);
        assertEquals("PLACED", result.getStatus());
        
        verify(trackingRepository, times(1)).save(any(OrderTracking.class));
        System.out.println(" testGetTrackingByOrderId_NoTracking_AutoCreates passed");
    }

    
    @Test
    public void testGetTrackingByOrderId_OrderNotFound() {
        when(trackingRepository.findByOrderId(99L)).thenReturn(Optional.empty());
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            trackingService.getTrackingByOrderId(99L)
        );
        System.out.println(" testGetTrackingByOrderId_OrderNotFound passed");
    }

    
    @Test
    public void testUpdateTracking_ExistingRecord_UpdatesStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(trackingRepository.findByOrderId(1L)).thenReturn(Optional.of(tracking));
        when(trackingRepository.save(any(OrderTracking.class))).thenReturn(tracking);

        TrackingDTO result = trackingService.updateTracking(1L, "DISPATCHED", "Your order is on the way!");

        assertNotNull(result);
        // The tracking entity itself should have been mutated to DISPATCHED
        assertEquals(OrderTracking.TrackingStatus.DISPATCHED, tracking.getStatus());
        assertEquals("Your order is on the way!", tracking.getMessage());

        verify(trackingRepository, times(1)).save(tracking);
        System.out.println("testUpdateTracking_ExistingRecord_UpdatesStatus passed");
    }

    
    @Test
    public void testUpdateTracking_NullMessage_UsesDefaultMessage() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(trackingRepository.findByOrderId(1L)).thenReturn(Optional.of(tracking));
        when(trackingRepository.save(any(OrderTracking.class))).thenReturn(tracking);

        TrackingDTO result = trackingService.updateTracking(1L, "CONFIRMED", null);

        assertNotNull(result);
        assertEquals(OrderTracking.TrackingStatus.CONFIRMED, tracking.getStatus());
        // Default message for CONFIRMED = "Restaurant has confirmed your order!"
        assertEquals("Restaurant has confirmed your order!", tracking.getMessage());
        System.out.println(" testUpdateTracking_NullMessage_UsesDefaultMessage passed");
    }

    
    @Test
    public void testUpdateTracking_NoExistingRecord_CreatesNew() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(trackingRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(trackingRepository.save(any(OrderTracking.class))).thenReturn(tracking);

        TrackingDTO result = trackingService.updateTracking(1L, "PROCESSING", "Cooking started!");

        assertNotNull(result);
        verify(trackingRepository, times(1)).save(any(OrderTracking.class));
        System.out.println("testUpdateTracking_NoExistingRecord_CreatesNew passed");
    }

    
    @Test
    public void testUpdateTracking_InvalidStatus_ThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () ->
            trackingService.updateTracking(1L, "FLYING", "Invalid status")
        );

        verify(trackingRepository, never()).save(any(OrderTracking.class));
        System.out.println("testUpdateTracking_InvalidStatus_ThrowsException passed");
    }

    
    @Test
    public void testUpdateTracking_OrderNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            trackingService.updateTracking(99L, "CONFIRMED", null)
        );

        verify(trackingRepository, never()).save(any(OrderTracking.class));
        System.out.println("testUpdateTracking_OrderNotFound passed");
    }
}