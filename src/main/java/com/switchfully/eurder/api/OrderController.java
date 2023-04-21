package com.switchfully.eurder.api;



import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.orderComponent.IOrderService;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
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

    @Autowired
    public OrderController(IOrderService orderService, ICustomerService customerService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAuthority('member')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json",consumes = "application/json")
    List<OrderDTO> orderItem(@RequestBody CreateOrderDTO createOrderDTO,@AuthenticationPrincipal Jwt jwt){
        return orderService.orderItems(createOrderDTO,jwt);
    }

    @PreAuthorize("hasAuthority('member')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/my-orders")
    String reportOrdersByCustomer(@AuthenticationPrincipal Jwt jwt){
        return orderService.reportOrdersByCustomer(jwt);
    }

    @PreAuthorize("hasAuthority('member')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", path = "/{id}/reorder")
    OrderDTO reorderOrder(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id){
        return orderService.reorderExistingOrder(id,jwt);
    }

    @PreAuthorize("hasAuthority('manager')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/shipping-list")
    String getTodaysShippingList(){
        return orderService.getShippingList();
    }

}
