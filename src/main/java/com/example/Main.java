package com.example;

import com.example.model.*;
import com.example.engine.Matcher;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    private static final List<Order> buyOrders = new ArrayList<>();
    private static final List<Order> sellOrders = new ArrayList<>();
    private static final List<String> tradeHistory = new ArrayList<>();
    private static final List<Trade> trades = new ArrayList<>();
    private static int orderIdCounter = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter order (or EXIT): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("EXIT")) break;

            try {
                Order order = parseOrder(input);

                if (order.getSide().equalsIgnoreCase("BUY")) {
                    buyOrders.add(order);
                    buyOrders.sort((a, b) -> {
                        int cmp = Double.compare(b.getPrice(), a.getPrice());
                        return (cmp == 0) ? a.getTimestamp().compareTo(b.getTimestamp()) : cmp;
                    });
                } else {
                    sellOrders.add(order);
                    sellOrders.sort((a, b) -> {
                        int cmp = Double.compare(a.getPrice(), b.getPrice());
                        return (cmp == 0) ? a.getTimestamp().compareTo(b.getTimestamp()) : cmp;
                    });
                }

                Matcher.matchOrders(buyOrders, sellOrders, tradeHistory, trades);
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }

        System.out.println("\n--- Remaining Orders ---");
        for (Order o : buyOrders) System.out.println("Unmatched BUY: " + o);
        for (Order o : sellOrders) System.out.println("Unmatched SELL: " + o);
    }

    private static Order parseOrder(String input) {
        String[] parts = input.trim().split(" ");
        String side = parts[0].toUpperCase();
        int quantity = Integer.parseInt(parts[1]);
        String[] priceParts = parts[2].split("@");
        double price = Double.parseDouble(priceParts[1]);
        OrderType type = (parts.length >= 4) ? OrderType.valueOf(parts[3].toUpperCase()) : OrderType.LIMIT;

        return new Order(orderIdCounter++, side, price, quantity, type);
    }
}