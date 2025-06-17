package com.example.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Trade {
    private final Order buyOrder;
    private final Order sellOrder;
    private final int quantity;
    private final double price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private final LocalDateTime timestamp;

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public Trade(Order buyOrder, Order sellOrder, int quantity, double price) {
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public Order getBuyOrder() { return buyOrder; }
    public Order getSellOrder() { return sellOrder; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("BUY(%d)-SELL(%d) %d @ %.2f %s",
                buyOrder.getOrderId(),
                sellOrder.getOrderId(),
                quantity,
                price,
                timestamp.format(DISPLAY_FORMATTER));
    }
}

