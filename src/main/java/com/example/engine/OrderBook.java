package com.example.engine;

import com.example.model.Order;
import com.example.model.OrderType;
import com.example.model.Trade;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderBook {
    private final List<Order> buyOrders = new ArrayList<>();
    private final List<Order> sellOrders = new ArrayList<>();
    private final List<String> tradeHistory = new ArrayList<>();
    private final List<Trade> trades = new ArrayList<>();

    public synchronized String addOrder(Order order) {
        if (order.getQuantity() <= 0) {
            return "Error: Quantity must be positive";
        }
        if (order.getPrice() <= 0 && order.getType() == OrderType.LIMIT) {
            return "Error: Limit orders must have positive price";
        }

        String receivedMessage = "Order received: " + order;

        if (order.getSide().equalsIgnoreCase("BUY")) {
            buyOrders.add(order);
            buyOrders.sort(this::compareBuyOrders);
        } else {
            sellOrders.add(order);
            sellOrders.sort(this::compareSellOrders);
        }

        matchOrders();
        return receivedMessage;
    }

    private int compareBuyOrders(Order a, Order b) {
        int cmp = Double.compare(b.getPrice(), a.getPrice());
        return (cmp == 0) ? a.getTimestamp().compareTo(b.getTimestamp()) : cmp;
    }

    private int compareSellOrders(Order a, Order b) {
        int cmp = Double.compare(a.getPrice(), b.getPrice());
        return (cmp == 0) ? a.getTimestamp().compareTo(b.getTimestamp()) : cmp;
    }

    private void matchOrders() {
        while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
            Order buy = buyOrders.get(0);
            Order sell = sellOrders.get(0);

            if (!canMatch(buy, sell)) break;

            int tradeQty = Math.min(buy.getRemainingQuantity(), sell.getRemainingQuantity());
            if (tradeQty <= 0) break;  // Prevent zero-quantity trades

            double tradePrice = determineTradePrice(buy, sell);
            executeTrade(buy, sell, tradeQty, tradePrice);
            removeFilledOrders();
        }
    }

    private boolean canMatch(Order buy, Order sell) {
        boolean isBuyMarket = buy.getType() == OrderType.MARKET;
        boolean isSellMarket = sell.getType() == OrderType.MARKET;
        return isBuyMarket || isSellMarket || buy.getPrice() >= sell.getPrice();
    }

    private double determineTradePrice(Order buy, Order sell) {
        if (buy.getType() == OrderType.MARKET) return sell.getPrice();
        if (sell.getType() == OrderType.MARKET) return buy.getPrice();
        return sell.getPrice(); // Use seller's price for limit orders
    }

    private void executeTrade(Order buy, Order sell, int qty, double price) {
        buy.fill(qty);
        sell.fill(qty);

        Trade trade = new Trade(buy, sell, qty, price);
        trades.add(trade);
        tradeHistory.add(trade.toString());
    }

    private void removeFilledOrders() {
        buyOrders.removeIf(Order::isFilled);
        sellOrders.removeIf(Order::isFilled);
    }

    public String printOrderBook() {
        StringBuilder sb = new StringBuilder("--- ORDER BOOK ---\n");
        buyOrders.forEach(o -> sb.append("BUY: ").append(o).append("\n"));
        sellOrders.forEach(o -> sb.append("SELL: ").append(o).append("\n"));
        return sb.toString();
    }

    public List<String> getTradeHistory() {
        return tradeHistory;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public List<Order> getBuyOrders() {
        return buyOrders;
    }

    public List<Order> getSellOrders() {
        return sellOrders;
    }
}
