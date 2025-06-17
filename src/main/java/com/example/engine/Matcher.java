package com.example.engine;

import com.example.model.Order;
import com.example.model.OrderType;
import com.example.model.Trade;

import java.util.List;

public class Matcher {
    public static void matchOrders(List<Order> buyOrders, List<Order> sellOrders, List<String> tradeHistory, List<Trade> trades) {
        while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
            Order buy = buyOrders.get(0);
            Order sell = sellOrders.get(0);

            boolean isBuyMarket = buy.getType() == OrderType.MARKET;
            boolean isSellMarket = sell.getType() == OrderType.MARKET;
            boolean priceMatch = buy.getPrice() >= sell.getPrice();
            boolean canTrade = isBuyMarket || isSellMarket || priceMatch;

            if (!canTrade) break;

            int tradedQty = Math.min(buy.getQuantity(), sell.getQuantity());
            double tradePrice = isBuyMarket ? sell.getPrice() :
                    isSellMarket ? buy.getPrice() : sell.getPrice();

            String tradeInfo = "TRADE: BuyID " + buy.getOrderId() +
                    " <--> SellID " + sell.getOrderId() +
                    " | " + tradedQty + " @ " + tradePrice;

            tradeHistory.add(tradeInfo);
            trades.add(new Trade(buy, sell, tradedQty, tradePrice));

            buy.setQuantity(buy.getQuantity() - tradedQty);
            sell.setQuantity(sell.getQuantity() - tradedQty);

            if (buy.getQuantity() == 0) buyOrders.remove(0);
            if (sell.getQuantity() == 0) sellOrders.remove(0);
        }
    }
}