package com.siddh.order_service.controller;

import com.siddh.order_service.annotation.TrackExecutionTime;
import com.siddh.order_service.dto.OrderRequestDTO;
import com.siddh.order_service.model.Order;
import com.siddh.order_service.repository.OrderRepository;
import com.siddh.order_service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository){
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/orders")
    @TrackExecutionTime
    public ResponseEntity<Map<String,String>> placeOrder(@RequestBody OrderRequestDTO incomingRequest){
        Order newOrder=new Order(
                incomingRequest.getAccountId(),
                incomingRequest.getSymbol(),
                incomingRequest.getQuantity(),
                incomingRequest.getPrice(),
                incomingRequest.getSide()
        );

        orderService.placeOrder(newOrder);
        Map<String,String>response=new LinkedHashMap<>();
        response.put("message","Order received and is processing in the background.");
        response.put("orderId",newOrder.getOrderId());
        response.put("status",newOrder.getStatus().name());

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/allOrders")
    public ResponseEntity<List<Order>>getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAllOrdersList());
    }
}

