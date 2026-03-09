package com.siddh.analytics_server.controller;

import com.siddh.analytics_server.client.OrderClient;
import com.siddh.analytics_server.model.Order;
import com.siddh.analytics_server.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class AnalyticsController {
    @Autowired
    OrderClient orderClient;

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics(){
        List<Order> orders=orderClient.getAllOrders();
        Map<String,Object>stats=new LinkedHashMap<>();

        //total order amount
        double totalAmount=orders.stream()
                .mapToDouble(o->o.getPrice()*o.getQuantity())
                .sum();
        stats.put("TotalOrderAmount",totalAmount);

        //total buy vs sell
        Map<String,Long>buyVsSell=orders.stream()
                .collect(Collectors.groupingBy(Order::getSide,Collectors.counting()));
        stats.put("TotalBuyVsSell",buyVsSell);

        //Group Order by Status
        Map<OrderStatus,Long>ordersByStatus=orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus,Collectors.counting()));
        stats.put("OrdersByStatus",ordersByStatus);

        //Top Customer by volume
        String topCustomer=orders.stream()
                .collect(Collectors.groupingBy(Order::getAccountId,
                        Collectors.summingDouble(o->o.getPrice()*o.getQuantity())))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Customers Yet");

        stats.put("TopCustomer",topCustomer);

        return ResponseEntity.ok(stats);
    }
}
