package com.switchfully.eurder.api;


import com.switchfully.eurder.components.orderComponent.IOrderService;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "orders")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json",consumes = "application/json", path = "/order")
    List<OrderDTO> orderItem(@RequestBody CreateOrderDTO createOrderDTO,@RequestHeader String authorization){
        return orderService.orderItems(createOrderDTO,authorization);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/my-order")
    String reportOrdersByCustomer(@RequestHeader String authorization){
        return orderService.reportOrdersByCustomer(authorization);
    }
}
