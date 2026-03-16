package com.siddh.order_service.service;

import com.siddh.order_service.config.CustomRejectHandler;
import com.siddh.order_service.config.CustomThreadFactory;
import com.siddh.order_service.dto.OrderRequestDTO;
import com.siddh.order_service.dto.OrderResponseDTO;
import com.siddh.order_service.entity.OrderEntity;
import com.siddh.order_service.model.Order;
import com.siddh.order_service.model.OrderStatus;
import com.siddh.order_service.repository.OrderRepository;
import jakarta.annotation.PreDestroy;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ExecutorService executorService;
    private static final String LOG_FILE="orders_log.txt";

    public OrderService(OrderRepository orderRepository){
        this.orderRepository=orderRepository;
        this.executorService=new ThreadPoolExecutor(3,5,10, TimeUnit.MINUTES,new ArrayBlockingQueue<>(3),new CustomThreadFactory(),new CustomRejectHandler());
    }

    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO){
//        orderRepository.save(order);
//        executorService.submit(()->processOrder(order));

        OrderEntity newOrder=new OrderEntity();
        newOrder.setAccountId(orderRequestDTO.getAccountId());
        newOrder.setPrice(orderRequestDTO.getPrice());
        newOrder.setQuantity(orderRequestDTO.getQuantity());
        newOrder.setSide(orderRequestDTO.getSide());
        newOrder.setSymbol(orderRequestDTO.getSymbol());
        newOrder.setTimestamp(LocalDateTime.now());
        newOrder.setOrderStatus(OrderStatus.PENDING);

        OrderEntity savedOrder = orderRepository.save(newOrder);

        OrderResponseDTO responseDTO=new OrderResponseDTO();
        responseDTO.setId(savedOrder.getId());
        responseDTO.setAccountId(savedOrder.getAccountId());
        responseDTO.setPrice(savedOrder.getPrice());
        responseDTO.setQuantity(savedOrder.getQuantity());
        responseDTO.setSide(savedOrder.getSide());
        responseDTO.setSymbol(savedOrder.getSymbol());
        responseDTO.setTimestamp(savedOrder.getTimestamp());
        responseDTO.setStatus(savedOrder.getOrderStatus());

        executorService.submit(()->processOrder(savedOrder));

        return responseDTO;
    }

    public void processOrder(OrderEntity order){
        try{
            Thread.sleep(1000);
            order.setOrderStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
            writeToFile(order);
        }
        catch (InterruptedException e){
            order.setOrderStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            Thread.currentThread().interrupt();
        }
    }

    public List<OrderResponseDTO> findAllOrders() {
        List<OrderEntity> entities=orderRepository.findAll();

        return entities.stream().map(entity -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setId(entity.getId());
            dto.setAccountId(entity.getAccountId());
            dto.setSymbol(entity.getSymbol());
            dto.setQuantity(entity.getQuantity());
            dto.setPrice(entity.getPrice());
            dto.setSide(entity.getSide());
            dto.setStatus(entity.getOrderStatus());
            dto.setTimestamp(entity.getTimestamp());
            return dto;
        }).collect(Collectors.toList());
    }



    public synchronized void writeToFile(OrderEntity order){
        try(BufferedWriter writer=new BufferedWriter(new FileWriter(LOG_FILE,true))){
            String logEntry=String.format("Time: %s | OrderID: %s | Account: %s | %s %d %s @ $%.2f | Status: %s\n",
                    order.getTimestamp(), order.getId(), order.getAccountId(),
                    order.getSide(), order.getQuantity(), order.getSymbol(), order.getPrice(), order.getOrderStatus());

            writer.write(logEntry);
        }
        catch (IOException ex){
            System.err.println("Failed to write log to file: "+ex.getMessage());
        }
    }

    @PreDestroy
    public void shutdown(){
        executorService.shutdown();
    }


}
