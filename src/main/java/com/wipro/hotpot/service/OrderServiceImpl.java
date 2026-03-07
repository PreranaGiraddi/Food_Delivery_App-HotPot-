package com.wipro.hotpot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.hotpot.dto.OrderDTO;
import com.wipro.hotpot.entity.Cart;
import com.wipro.hotpot.entity.CartItem;
import com.wipro.hotpot.entity.Order;
import com.wipro.hotpot.entity.OrderItem;
import com.wipro.hotpot.entity.OrderTracking;
import com.wipro.hotpot.entity.Restaurant;
import com.wipro.hotpot.entity.User;
import com.wipro.hotpot.exception.ResourceNotFoundException;
import com.wipro.hotpot.repository.ICartItemRepository;
import com.wipro.hotpot.repository.ICartRepository;
import com.wipro.hotpot.repository.IOrderRepository;
import com.wipro.hotpot.repository.IRestaurantRepository;
import com.wipro.hotpot.repository.ITrackingRepository;
import com.wipro.hotpot.repository.IUserRepository;
import com.wipro.hotpot.entity.OrderTracking;
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRestaurantRepository restaurantRepository;

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private ICartItemRepository cartItemRepository;

    @Autowired
    private ICartService cartService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ITrackingRepository trackingRepository;


    @Override
    @Transactional
    public Order placeOrder(Long userId, OrderDTO dto) {

        System.out.println("=== PLACE ORDER START ===");
        System.out.println("UserId: " + userId);

        // Step 1 — Find user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        // Step 2 — Find restaurant
        Restaurant restaurant = restaurantRepository
            .findById(dto.getRestaurantId())
            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found!"));

        // Step 3 — Get cart
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found!"));

        System.out.println("CartId: " + cart.getId());

        // Step 4 — Get cart items FRESH from DB
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        System.out.println("Cart items found: " + cartItems.size());

        // Step 5 — Check cart not empty
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty! Add items before ordering.");
        }

        // Step 6 — Check all items from same restaurant
        for (CartItem cartItem : cartItems) {

            if (cartItem.getMenuItem() == null
                    || cartItem.getMenuItem().getRestaurant() == null) {
                continue;
            }

            Long itemRestaurantId = cartItem.getMenuItem()
                                            .getRestaurant()
                                            .getId();

            if (!itemRestaurantId.equals(dto.getRestaurantId())) {
                throw new RuntimeException(
                    "Cart has items from multiple restaurants! " +
                    "Please clear cart and order from one restaurant."
                );
            }
        }

        // Step 7 — Create order
        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setTotalAmount(cart.getTotalPrice());
        order.setStatus(Order.OrderStatus.PLACED);

        // Step 8 — Convert cart items → order items
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(cartItem.getMenuItem());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getTotalItemPrice());
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);

        // Step 9 — Save order
        Order savedOrder = orderRepository.save(order);

        System.out.println("✅ Order saved! OrderId: " + savedOrder.getId());

        // Step 10 — Create tracking record automatically
        try {
            OrderTracking tracking = new OrderTracking();
            tracking.setOrder(savedOrder);
            tracking.setStatus(OrderTracking.TrackingStatus.PLACED);
            tracking.setMessage("Your order has been placed successfully!");
            trackingRepository.save(tracking);
            System.out.println("✅ Tracking created!");
        } catch (Exception e) {
            System.out.println("❌ Tracking failed: " + e.getMessage());
        }

        // ✅ Step 11 — Clear cart CORRECTLY
        // CORRECT ORDER: Delete from DB first, then clear in-memory list
        System.out.println("Deleting cart items...");

        // 11a — Fetch fresh list and delete from DB first
        List<CartItem> itemsToDelete = cartItemRepository.findByCartId(cart.getId());
        System.out.println("Items to delete: " + itemsToDelete.size());

        if (!itemsToDelete.isEmpty()) {
            cartItemRepository.deleteAll(itemsToDelete);
            cartItemRepository.flush(); // ✅ Force immediate DB commit
        }

        // 11b — Now clear in-memory list and reset total
        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.saveAndFlush(cart); // ✅ Save clean cart to DB

        // 11c — Verify deletion (for debugging)
        List<CartItem> remaining = cartItemRepository.findByCartId(cart.getId());
        System.out.println("Items remaining after delete: " + remaining.size());
        System.out.println("✅ Cart cleared!");

        // Step 12 — Send confirmation email
        try {
            emailService.sendOrderConfirmationEmail(
                user.getEmail(),
                user.getName(),
                savedOrder.getId()
            );
            System.out.println("✅ Email sent!");
        } catch (Exception e) {
            System.out.println("❌ Email failed: " + e.getMessage());
        }

        System.out.println("=== ORDER COMPLETE ===");

        return savedOrder;
    }


    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Order not found with id: " + orderId));
    }


    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }


    @Override
    public List<Order> getOrdersByRestaurant(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }


    @Override
    public List<Order> getOrderHistory(Long userId) {
        return orderRepository.findOrderHistoryByUser(userId);
    }


    @Override
    public Order cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);

        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("Delivered orders cannot be cancelled!");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));

        // ✅ Validate status value
        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status +
                ". Valid values: CONFIRMED, PROCESSING, DISPATCHED, DELIVERED, CANCELLED");
        }

        Order saved = orderRepository.save(order);

        // ✅ Also update tracking record if exists
        try {
            trackingRepository.findByOrderId(orderId).ifPresent(tracking -> {
                tracking.setStatus(OrderTracking.TrackingStatus.valueOf(status.toUpperCase()));
                tracking.setMessage(getTrackingMessage(status));
                trackingRepository.save(tracking);
            });
        } catch (Exception e) {
            System.out.println("Tracking update skipped: " + e.getMessage());
        }

        return saved;
    }

    // ✅ Helper — tracking message for each status
    private String getTrackingMessage(String status) {
        switch (status.toUpperCase()) {
            case "CONFIRMED":  return "Your order has been confirmed by the restaurant!";
            case "PROCESSING": return "Your order is being prepared in the kitchen!";
            case "DISPATCHED": return "Your order is on the way! Delivery partner assigned.";
            case "DELIVERED":  return "Order delivered successfully! Enjoy your meal 🎉";
            case "CANCELLED":  return "Your order has been cancelled.";
            default:           return "Order status updated.";
        }
    }
}