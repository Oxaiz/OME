package com.example.engine;

import com.example.model.Order;
import java.util.ArrayList;
import java.util.List;

public class OrderBook {
    private final List<Order> buyOrders = new ArrayList<>();
    private final List<Order> sellOrders = new ArrayList<>();

    public void addOrder(Order order) {
        if ("buy".equalsIgnoreCase(order.getType())) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
    }

    public List<Order> getBuyOrders() { return buyOrders; }
    public List<Order> getSellOrders() { return sellOrders; }
}
