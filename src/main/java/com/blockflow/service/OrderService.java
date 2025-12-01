package com.blockflow.service;

import com.blockflow.model.Order;
import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order createOrder(Order order);

    void deleteOrder(Long id);
} // End of interface
