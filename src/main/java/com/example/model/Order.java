package com.example.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.concurrent.atomic.AtomicLong;

public class Order {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
    private long orderId;
    private String side;
    private double price;
    private int quantity;
    private int remainingQuantity;
    private OrderType type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime timestamp;

    public static long generateId() {
        return ID_GENERATOR.getAndIncrement();
    }

    // CHANGED: Proper ID generation in no-arg constructor
    public Order() {
        this.orderId = generateId();
        this.timestamp = LocalDateTime.now();
    }

    // CHANGED: Now respects passed orderId
    public Order(long orderId, String side, double price, int quantity, OrderType type) {
        this();
        this.orderId = orderId; // Now using the provided orderId
        this.side = side;
        this.price = price;
        this.quantity = quantity;
        this.remainingQuantity = quantity;
        this.type = type;
    }

    // Business methods
    public void fill(int quantity) {
        if (quantity > remainingQuantity) {
            throw new IllegalArgumentException("Cannot fill more than remaining quantity");
        }
        remainingQuantity -= quantity;
    }

    public boolean isFilled() {
        return remainingQuantity == 0;
    }

    // Getters and setters
    public long getOrderId() { return orderId; }
    public String getSide() { return side; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getRemainingQuantity() { return remainingQuantity; }
    public OrderType getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setOrderId(long orderId) { this.orderId = orderId; }
    public void setSide(String side) { this.side = side; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.remainingQuantity = quantity;
    }
    public void setType(OrderType type) { this.type = type; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return String.format("%s %d @ %.2f (%d) [%s]",
                side, remainingQuantity , price, orderId, type);
    }
}
