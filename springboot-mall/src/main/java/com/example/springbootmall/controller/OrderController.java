package com.example.springbootmall.controller;

import com.example.springbootmall.dto.CreateOrderRequest;
import com.example.springbootmall.dto.OrderQueryParams;
import com.example.springbootmall.model.Order;
import com.example.springbootmall.service.OrderService;
import com.example.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 建議將路徑改為支持「選填」userId，或另外開一個給賣家的路徑
    @GetMapping("/orders") // 這樣前端 call /api/orders 才會通
    public ResponseEntity<Page<Order>> getAllOrders(
            @RequestParam(required = false) Integer userId, // 變成選填
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "CUSTOMER") String role
    ) {
        OrderQueryParams orderQueryParams = new OrderQueryParams();

        // 如果是賣家，不管有沒有 userId，都查全部
        if ("SELLER".equals(role)) {
            orderQueryParams.setUserId(null);
        } else {
            orderQueryParams.setUserId(userId);
        }

        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        List<Order> orderList = orderService.getOrders(orderQueryParams);
        Integer count = orderService.countOrder(orderQueryParams);

        Page<Order> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }


    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest) {
        Integer orderId = orderService.createOrder(userId, createOrderRequest);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @DeleteMapping("/users/{userId}/orders/{orderId}")
    public ResponseEntity<?> deleteOrderById(@PathVariable Integer userId,
                                             @PathVariable Integer orderId) {
        orderService.deleteOrderById(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
