package com.example.model;

public class Order {
    private String id;
    private String type; // buy or sell
    private int quantity;
    private double price;

    public Order(String id, String type, int quantity, double price) {
        this.id = id;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("Order{id='%s', type='%s', qty=%d, price=%.2f}", id, type, quantity, price);
    }
}
