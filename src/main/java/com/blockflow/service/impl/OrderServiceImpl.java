package com.blockflow.service.impl;

import com.blockflow.exception.ResourceNotFoundException;
import com.blockflow.model.Order;
import com.blockflow.model.Product;
import com.blockflow.repository.OrderRepository;
import com.blockflow.repository.ProductRepository;
import com.blockflow.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        log.info("Fetching order with id: {}", id);
        return orderRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        log.info("Creating new order for customer: {}", order.getCustomerName());

        if (order.getTotalAmount() == null) {
            Product product = productRepository.findById(Objects.requireNonNull(order.getProductId()))
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Product not found with id: " + order.getProductId()));

            BigDecimal total = product.getPricePerUnit().multiply(BigDecimal.valueOf(order.getQuantity()));
            order.setTotalAmount(total);
        }

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        log.info("Deleting order with id: {}", id);
        Long nonNullId = Objects.requireNonNull(id);
        if (!orderRepository.existsById(nonNullId)) {
            throw new ResourceNotFoundException("Order not found with id: " + nonNullId);
        }
        orderRepository.deleteById(nonNullId);
    }
} // End of class
