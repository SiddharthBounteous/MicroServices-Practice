package com.siddh.order_service.controller;

import com.siddh.order_service.annotation.TrackExecutionTime;
import com.siddh.order_service.dto.OrderRequestDTO;
import com.siddh.order_service.dto.OrderResponseDTO;
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

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    @TrackExecutionTime
    public ResponseEntity<Map<String,String>> placeOrder(@RequestBody OrderRequestDTO incomingRequest){


        OrderResponseDTO orderResponseDTO=orderService.placeOrder(incomingRequest);
        Map<String,String>response=new LinkedHashMap<>();
        response.put("message","Order received and is processing in the background.");
        response.put("orderId",String.valueOf(orderResponseDTO.getId()));
        response.put("status",orderResponseDTO.getStatus().name());

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/allOrders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }
}

