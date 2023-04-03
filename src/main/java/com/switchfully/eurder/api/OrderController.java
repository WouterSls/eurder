package com.switchfully.eurder.api;


import com.switchfully.eurder.utils.Feature;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.orderComponent.IOrderService;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "orders")
public class OrderController {

    private final IOrderService orderService;
    private final ICustomerService customerService;

    @Autowired
    public OrderController(IOrderService orderService, ICustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json",consumes = "application/json", path = "/order")
    List<OrderDTO> orderItem(@RequestBody CreateOrderDTO createOrderDTO,@RequestHeader String authorization){
        customerService.validateAuthorization(authorization, Feature.CREATE_NEW_ORDER);
        return orderService.orderItems(createOrderDTO,authorization);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/my-orders")
    String reportOrdersByCustomer(@RequestHeader String authorization){
        customerService.validateAuthorization(authorization,Feature.VIEW_MY_ORDERS);
        return orderService.reportOrdersByCustomer(authorization);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", path = "/{id}/reorder")
    OrderDTO reorderOrder(@RequestHeader String authorization, @PathVariable UUID id){
        customerService.validateAuthorization(authorization,Feature.REORDER_ORDER);
        return orderService.reorderExistingOrder(id,authorization);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/shipping-list")
    String getTodaysShippingList(@RequestHeader String authorization){
        customerService.validateAuthorization(authorization,Feature.GET_SHIPPING_LIST);
        return orderService.getShippingList();
    }

}
