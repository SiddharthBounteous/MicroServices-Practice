package com.siddh.analytics_server.client;

import com.siddh.analytics_server.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderClient {
    @GetMapping("/api/v1/allOrders")
    List<Order> getAllOrders();
}
