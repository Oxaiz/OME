package com.example.controller;

import com.example.engine.OrderBook;
import com.example.model.Order;
import com.example.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderBook orderBook;

    @Autowired
    public OrderController(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    @PostMapping("/orders")
    public String placeOrder(@RequestBody Order order) {
        if (order.getOrderId() == 0) {
            order.setOrderId(Order.generateId()); // Now using the correct method
        }
        return orderBook.addOrder(order);
    }
    @GetMapping("/orderbook")
    public String getOrderBook() {
        return orderBook.printOrderBook();
    }

    @GetMapping("/trades")
    public List<Trade> getTrades() {
        return orderBook.getTrades();
    }
}


