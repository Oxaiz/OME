package com.example.controller;

import com.example.engine.OrderBook;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TradeLogController {
    private final OrderBook orderBook;

    public TradeLogController(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    @GetMapping("/trades-Log")
    public List<String> getTrades() {
        return orderBook.getTradeHistory();
    }
}




