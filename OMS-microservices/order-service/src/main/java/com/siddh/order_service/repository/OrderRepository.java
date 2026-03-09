package com.siddh.order_service.repository;

import com.siddh.order_service.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrderRepository {

    private final ConcurrentHashMap<String, Order> data=new ConcurrentHashMap<>();

    //save/update order
    public void saveOrder(Order order){
        data.put(order.getOrderId(),order);
    }

    //get order by id
    public Order getOrderById(String orderId){
        return data.get(orderId);
    }

    //list of all orders
    public List<Order> findAllOrdersList(){
        return new ArrayList<>(data.values());
    }
}
