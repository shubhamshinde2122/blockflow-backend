package com.blockflow.controller;

import com.blockflow.model.Order;
import com.blockflow.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.DELETE, RequestMethod.PUT })
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        log.info("REST request to get all orders");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        log.info("REST request to get order : {}", id);
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        log.info("REST request to create order for customer : {}", order.getCustomerName());
        Order createdOrder = orderService.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("REST request to delete order : {}", id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        log.info("REST request to update order : {}", id);
        // getOrderById throws ResourceNotFoundException if not found, so we don't need
        // Optional check here
        Order existingOrder = orderService.getOrderById(id);

        if (orderDetails.getProductId() != null) {
            existingOrder.setProductId(orderDetails.getProductId());
        }
        if (orderDetails.getCustomerName() != null) {
            existingOrder.setCustomerName(orderDetails.getCustomerName());
        }
        if (orderDetails.getQuantity() != null) {
            existingOrder.setQuantity(orderDetails.getQuantity());
        }
        if (orderDetails.getStatus() != null) {
            existingOrder.setStatus(orderDetails.getStatus());
        }
        Order updatedOrder = orderService.createOrder(existingOrder);
        return ResponseEntity.ok(updatedOrder);
    }
}
