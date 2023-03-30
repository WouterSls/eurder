package com.switchfully.eurder.components.OrderComponent;


import com.switchfully.eurder.components.ItemComponent.IItemService;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.ItemGroupDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import com.switchfully.eurder.exception.IllegalAmountException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final IItemService itemService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, IItemService itemService){
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.itemService = itemService;
    }

    @Override
    public List<OrderDTO> orderItems(CreateOrderDTO createOrderDTO){
        verifyOrder(createOrderDTO);
        List<Order> orders = createOrders(createOrderDTO);
        orderRepository.addOrders(orders);
        return orderMapper.mapToDTO(orders);
    }

    void verifyOrder(CreateOrderDTO createOrderDTO){
        if (createOrderDTO == null) {
            throw new MandatoryFieldException("Please provide an order");
        }
        if (createOrderDTO.getOrders() == null){
            throw new MandatoryFieldException("Please provide at least one ItemGroup");
        }

        if (createOrderDTO.getOrders().isEmpty()){
            throw new MandatoryFieldException("Please provide at least one ItemGroup");
        }

        for (ItemGroupDTO itemGroup :
                createOrderDTO.getOrders()) {
            if (itemGroup.getId() == null) {
                throw new MandatoryFieldException("Please provide an id for each order for all items");
            }
        }

        for (ItemGroupDTO itemGroup :
                createOrderDTO.getOrders()) {
            if (itemGroup.getAmountOrdered() <= 0){
                throw new IllegalAmountException("Please provide an amount bigger than 0 for all items");
            }
        }

        boolean amountOrderedBiggerThanAmountOfItems = createOrderDTO.getOrders().stream()
                .anyMatch(itemGroupDTO -> itemGroupDTO.getAmountOrdered() > itemService.getItemById(itemGroupDTO.getId()).getAmount());
        if (amountOrderedBiggerThanAmountOfItems){
            throw new IllegalAmountException("Not enough items in stock");
        }

    }

    private List<Order> createOrders(CreateOrderDTO createOrderDTO){
        List<Order> result = new ArrayList<>();

        for (ItemGroupDTO itemGroupDTO :
                createOrderDTO.getOrders()) {
            ItemDTO itemFromOrder = itemService.getItemById(itemGroupDTO.getId());
            result.add(new Order(itemFromOrder, itemGroupDTO.getAmountOrdered()));
        }

        return result;
    }

}
