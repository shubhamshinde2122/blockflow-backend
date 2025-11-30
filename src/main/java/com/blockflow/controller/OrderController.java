package com.blockflow.controller;

import com.blockflow.model.Order;
import com.blockflow.model.User;
import com.blockflow.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.DELETE })
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Order createOrder(@RequestBody Order order, Authentication auth) {
        User currentUser = (User) auth.getPrincipal();
        order.setUser(currentUser);
        return orderService.createOrder(order);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails,
            Authentication auth) {
        Order existingOrder = orderService.getOrderById(id);
        User currentUser = (User) auth.getPrincipal();

        boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;
        boolean isOwner = existingOrder.getUser() != null
                && existingOrder.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (orderDetails.getProductId() != null) {
            existingOrder.setProductId(orderDetails.getProductId());
        }
        if (orderDetails.getQuantity() != null) {
            existingOrder.setQuantity(orderDetails.getQuantity());
        }
        if (orderDetails.getCustomerName() != null) {
            existingOrder.setCustomerName(orderDetails.getCustomerName());
        }
        if (orderDetails.getStatus() != null) {
            existingOrder.setStatus(orderDetails.getStatus());
        }

        Order updatedOrder = orderService.createOrder(existingOrder);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id, Authentication auth) {
        Order existingOrder = orderService.getOrderById(id);
        User currentUser = (User) auth.getPrincipal();

        boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;
        boolean isOwner = existingOrder.getUser() != null
                && existingOrder.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
