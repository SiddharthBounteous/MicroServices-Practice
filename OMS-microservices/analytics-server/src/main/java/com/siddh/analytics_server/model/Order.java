package com.siddh.analytics_server.model;

import java.time.LocalDateTime;

public class Order {
    private String orderId;
    private String accountId;
    private String symbol;
    private int quantity;
    private double price;
    private String side;
    private OrderStatus status;
    private LocalDateTime timestamp;
}
