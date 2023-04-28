package com.switchfully.eurder.api;



import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.orderComponent.IOrderService;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import com.switchfully.eurder.components.orderComponent.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "orders")
public class OrderController {

    private final IOrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(IOrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PreAuthorize("hasAuthority('member')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json",consumes = "application/json")
    OrderDTO orderItem(@RequestBody CreateOrderDTO createOrderDTO, @AuthenticationPrincipal Jwt jwt){ return orderMapper.mapToDTO(orderService.orderItems(createOrderDTO,jwt)); }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/my-orders")
    String reportOrdersByCustomer(@AuthenticationPrincipal Jwt jwt){
        return orderService.reportOrdersByCustomer(jwt);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", path = "/{id}/reorder")
    OrderDTO reorderOrder(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id){
        return orderMapper.mapToDTO(orderService.reorderExistingOrder(id,jwt));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/shipping-list")
    String getTodaysShippingList(){
        return orderService.getShippingList();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json")
    List<OrderDTO> findAllOrders(){
        return orderMapper.mapToDTO(orderService.findAllOrders());
    }
}