package com.siddh.order_service.model;

import com.siddh.order_service.exception.InvalidOrderException;

import java.time.LocalDateTime;
import java.util.UUID;

public class Order {
    private String orderId;
    private String accountId;
    private String symbol;
    private int quantity;
    private double price;
    private String side;
    private OrderStatus status;
    private LocalDateTime timestamp;

    public Order(){}

    public Order(String accountId, String symbol, int quantity, double price, String side) {

        if(quantity<=0){
            throw new InvalidOrderException("Quantity must be greater than zero");
        }
        if(price<=0){
            throw new InvalidOrderException("Price must be greater than zero");
        }

        if(!side.equalsIgnoreCase("BUY") && !side.equalsIgnoreCase("SELL")){
            throw new InvalidOrderException("Not a valid Side");
        }

        this.orderId= UUID.randomUUID().toString();
        this.accountId = accountId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.timestamp=LocalDateTime.now();
        this.status=OrderStatus.PENDING;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getSide() {
        return side;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
