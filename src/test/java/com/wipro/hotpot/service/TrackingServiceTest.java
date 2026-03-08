package com.wipro.hotpot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    // NOTE: EmailService removed — TrackingServiceImpl does NOT @Autowire EmailService
    // Only ITrackingRepository and IOrderRepository are injected

    @InjectMocks
    private TrackingServiceImpl trackingService;

    private Order order;
    private OrderTracking tracking;
    private User user;
    private Restaurant restaurant;

    // ---------------------------------------------------------------
    // Setup — mirrors real DB rows
    // DB ref orders:   id=24, user_id=7, restaurant_id=1, total_amount=159
    // DB ref tracking: id=10, order_id=24, status=PLACED initially
    // DB ref users:    id=7, name="Prerana Giraddi", email="preranagiraddi@gmail.com"
    // DB ref restaurants: id=1, name="HotPot Restaurant"
    //
    // NOTE: order.setPaymentMethod() removed — TrackingServiceImpl does not
    //       use PaymentMethod at all, so no need to set it here
    // ---------------------------------------------------------------
    @BeforeEach
    public void setUp() {

        user = new User();
        user.setId(7L);
        user.setName("Prerana Giraddi");
        user.setEmail("preranagiraddi@gmail.com");

        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("HotPot Restaurant");

        order = new Order();
        order.setId(24L);                                       // DB: order id=24
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setStatus(Order.OrderStatus.PLACED);
        order.setTotalAmount(159.0);                            // DB: total_amount=159

        tracking = new OrderTracking();
        tracking.setId(10L);                                    // DB: tracking id=10
        tracking.setOrder(order);
        tracking.setStatus(OrderTracking.TrackingStatus.PLACED);
        tracking.setMessage("Your order has been placed successfully!");
        tracking.setUpdatedAt(LocalDateTime.now());
    }

    // ---------------------------------------------------------------
    // TEST 1: createTracking — success, no existing tracking
    // Impl: finds order → findByOrderId returns empty → saves new tracking
    // Returns TrackingDTO (NOT OrderTracking)
    // DB ref: order_id=24 exists, tracking created fresh
    // ---------------------------------------------------------------
    @Test
    public void testCreateTracking_Success() {

        when(orderRepository.findById(24L))
                .thenReturn(Optional.of(order));
        when(trackingRepository.findByOrderId(24L))
                .thenReturn(Optional.empty());                  // no existing tracking
        when(trackingRepository.save(any(OrderTracking.class)))
                .thenReturn(tracking);

        TrackingDTO result = trackingService.createTracking(24L);

        assertNotNull(result);
        assertEquals(24L, result.getOrderId());
        assertEquals("PLACED", result.getStatus());
        assertEquals("Your order has been placed successfully!", result.getMessage());

        verify(trackingRepository, times(1)).save(any(OrderTracking.class));

        System.out.println("✅ Create Tracking Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 2: createTracking — tracking already exists, returns existing DTO
    // Impl: findByOrderId returns present → returns toDTO(existing), NO save
    // DB ref: order_id=24 tracking id=10 already present
    // ---------------------------------------------------------------
    @Test
    public void testCreateTracking_AlreadyExists_ReturnsExisting() {

        when(orderRepository.findById(24L))
                .thenReturn(Optional.of(order));
        when(trackingRepository.findByOrderId(24L))
                .thenReturn(Optional.of(tracking));             // already exists

        TrackingDTO result = trackingService.createTracking(24L);

        assertNotNull(result);
        assertEquals(24L, result.getOrderId());
        assertEquals("PLACED", result.getStatus());

        verify(trackingRepository, never()).save(any(OrderTracking.class)); // no new save

        System.out.println("✅ Create Tracking Already Exists Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 3: createTracking — order not found
    // Impl throws: new RuntimeException("Order not found: " + orderId)
    // Using order_id=99 which does not exist in DB
    // ---------------------------------------------------------------
    @Test
    public void testCreateTracking_OrderNotFound() {

        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trackingService.createTracking(99L);
        });

        assertEquals("Order not found: 99", exception.getMessage()); // exact impl message
        verify(trackingRepository, never()).save(any(OrderTracking.class));

        System.out.println("✅ Create Tracking Order Not Found Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 4: updateTracking — to CONFIRMED with custom message
    // DB ref: order_id=20, tracking id=5, total_amount=78
    // Impl signature: updateTracking(Long orderId, String status, String message)
    // Returns TrackingDTO
    // ---------------------------------------------------------------
    @Test
    public void testUpdateTracking_ToConfirmed() {

        Order orderForConfirm = new Order();
        orderForConfirm.setId(20L);                             // DB: order_id=20
        orderForConfirm.setUser(user);
        orderForConfirm.setRestaurant(restaurant);
        orderForConfirm.setStatus(Order.OrderStatus.PLACED);
        orderForConfirm.setTotalAmount(78.0);                   // DB: total_amount=78

        OrderTracking confirmTracking = new OrderTracking();
        confirmTracking.setId(5L);                              // DB: tracking id=5
        confirmTracking.setOrder(orderForConfirm);
        confirmTracking.setStatus(OrderTracking.TrackingStatus.PLACED);
        confirmTracking.setMessage("Your order has been placed successfully!");
        confirmTracking.setUpdatedAt(LocalDateTime.now());

        when(orderRepository.findById(20L))
                .thenReturn(Optional.of(orderForConfirm));
        when(trackingRepository.findByOrderId(20L))
                .thenReturn(Optional.of(confirmTracking));
        when(trackingRepository.save(any(OrderTracking.class)))
                .thenReturn(confirmTracking);

        TrackingDTO result = trackingService.updateTracking(
                20L, "CONFIRMED", "Restaurant has confirmed your order!");

        assertNotNull(result);
        // Impl mutates confirmTracking before saving
        assertEquals(OrderTracking.TrackingStatus.CONFIRMED, confirmTracking.getStatus());
        assertEquals("Restaurant has confirmed your order!", confirmTracking.getMessage());

        verify(trackingRepository, times(1)).save(confirmTracking);

        System.out.println("✅ Update Tracking To CONFIRMED Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 5: updateTracking — to DISPATCHED with null message (uses default)
    // DB ref: order_id=13 has status=DISPATCHED
    // Impl getDefaultMessage("DISPATCHED") → "Your order has been dispatched!"
    // ---------------------------------------------------------------
    @Test
    public void testUpdateTracking_ToDispatched_DefaultMessage() {

        when(orderRepository.findById(24L))
                .thenReturn(Optional.of(order));
        when(trackingRepository.findByOrderId(24L))
                .thenReturn(Optional.of(tracking));
        when(trackingRepository.save(any(OrderTracking.class)))
                .thenReturn(tracking);

        // null message → impl falls back to getDefaultMessage()
        TrackingDTO result = trackingService.updateTracking(24L, "DISPATCHED", null);

        assertNotNull(result);
        assertEquals(OrderTracking.TrackingStatus.DISPATCHED, tracking.getStatus());
        assertEquals("Your order has been dispatched!", tracking.getMessage()); // default

        verify(trackingRepository, times(1)).save(tracking);

        System.out.println("✅ Update Tracking To DISPATCHED (default msg) Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 6: updateTracking — to DELIVERED
    // DB ref: tracking id=10, order_id=24, final state=DELIVERED
    // ---------------------------------------------------------------
    @Test
    public void testUpdateTracking_ToDelivered() {

        when(orderRepository.findById(24L))
                .thenReturn(Optional.of(order));
        when(trackingRepository.findByOrderId(24L))
                .thenReturn(Optional.of(tracking));
        when(trackingRepository.save(any(OrderTracking.class)))
                .thenReturn(tracking);

        TrackingDTO result = trackingService.updateTracking(
                24L, "DELIVERED", "Your order has been delivered. Enjoy your meal!");

        assertNotNull(result);
        assertEquals(OrderTracking.TrackingStatus.DELIVERED, tracking.getStatus());
        assertEquals("Your order has been delivered. Enjoy your meal!", tracking.getMessage());

        verify(trackingRepository, times(1)).save(tracking);

        System.out.println("✅ Update Tracking To DELIVERED Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 7: updateTracking — no existing tracking, impl creates a new one
    // Impl: findByOrderId returns empty → creates new OrderTracking object
    // ---------------------------------------------------------------
    @Test
    public void testUpdateTracking_NoExistingTracking_CreatesNew() {

        when(orderRepository.findById(24L))
                .thenReturn(Optional.of(order));
        when(trackingRepository.findByOrderId(24L))
                .thenReturn(Optional.empty());                  // no tracking yet
        when(trackingRepository.save(any(OrderTracking.class)))
                .thenReturn(tracking);

        TrackingDTO result = trackingService.updateTracking(24L, "CONFIRMED", null);

        assertNotNull(result);
        verify(trackingRepository, times(1)).save(any(OrderTracking.class));

        System.out.println("✅ Update Tracking Creates New When Missing Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 8: getTrackingByOrderId — tracking exists, returns DTO
    // DB ref: tracking id=10, order_id=24, status=PLACED
    // ---------------------------------------------------------------
    @Test
    public void testGetTrackingByOrderId_Exists() {

        when(trackingRepository.findByOrderId(24L))
                .thenReturn(Optional.of(tracking));

        TrackingDTO result = trackingService.getTrackingByOrderId(24L);

        assertNotNull(result);
        assertEquals(24L, result.getOrderId());
        assertEquals("PLACED", result.getStatus());
        assertEquals("Your order has been placed successfully!", result.getMessage());

        System.out.println("✅ Get Tracking By Order Id Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 9: getTrackingByOrderId — DELIVERED status
    // DB ref: final state of tracking id=10
    // ---------------------------------------------------------------
    @Test
    public void testGetTrackingByOrderId_Delivered() {

        tracking.setStatus(OrderTracking.TrackingStatus.DELIVERED);
        tracking.setMessage("Your order has been delivered. Enjoy your meal!");

        when(trackingRepository.findByOrderId(24L))
                .thenReturn(Optional.of(tracking));

        TrackingDTO result = trackingService.getTrackingByOrderId(24L);

        assertNotNull(result);
        assertEquals(24L, result.getOrderId());
        assertEquals("DELIVERED", result.getStatus());
        assertEquals("Your order has been delivered. Enjoy your meal!", result.getMessage());

        System.out.println("✅ Get Tracking By Order Id (DELIVERED) Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 10: getTrackingByOrderId — no tracking exists, impl auto-creates PLACED
    // Impl: findByOrderId empty → findById(order) → save new PLACED tracking
    // DB edge case: order exists but tracking row was never created
    // ---------------------------------------------------------------
    @Test
    public void testGetTrackingByOrderId_NoTracking_AutoCreates() {

        when(trackingRepository.findByOrderId(24L))
                .thenReturn(Optional.empty());
        when(orderRepository.findById(24L))
                .thenReturn(Optional.of(order));
        when(trackingRepository.save(any(OrderTracking.class)))
                .thenReturn(tracking);

        TrackingDTO result = trackingService.getTrackingByOrderId(24L);

        assertNotNull(result);
        assertEquals("PLACED", result.getStatus());
        assertEquals("Your order has been placed successfully!", result.getMessage());

        verify(trackingRepository, times(1)).save(any(OrderTracking.class));

        System.out.println("✅ Get Tracking Auto-Create Fallback Test Passed!");
    }

    // ---------------------------------------------------------------
    // TEST 11: getTrackingByOrderId — order not found at all
    // Impl throws: new RuntimeException("Order not found: " + orderId)
    // Using order_id=99 which does not exist in DB
    // ---------------------------------------------------------------
    @Test
    public void testGetTrackingByOrderId_OrderNotFound() {

        when(trackingRepository.findByOrderId(99L))
                .thenReturn(Optional.empty());
        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trackingService.getTrackingByOrderId(99L);
        });

        assertEquals("Order not found: 99", exception.getMessage()); // exact impl message

        System.out.println("✅ Get Tracking Order Not Found Test Passed!");
    }
}