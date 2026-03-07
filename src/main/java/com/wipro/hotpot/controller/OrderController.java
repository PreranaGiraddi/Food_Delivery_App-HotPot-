package com.wipro.hotpot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.hotpot.dto.OrderDTO;
import com.wipro.hotpot.entity.Order;
import com.wipro.hotpot.service.IOrderService;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

  
    @PostMapping("/place/{userId}")
    public ResponseEntity<Order> placeOrder(@PathVariable Long userId,
                                            @Valid @RequestBody OrderDTO dto) {
        Order order = orderService.placeOrder(userId, dto);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

   
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

  
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getOrdersByRestaurant(
            @PathVariable Long restaurantId) {
        List<Order> orders = orderService.getOrdersByRestaurant(restaurantId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Order>> getOrderHistory(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrderHistory(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

   
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        Order order = orderService.cancelOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @PutMapping("/status/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId,
                                                   @RequestParam String status) {
        Order order = orderService.updateOrderStatus(orderId, status);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}