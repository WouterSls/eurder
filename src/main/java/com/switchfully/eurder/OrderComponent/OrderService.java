package com.switchfully.eurder.OrderComponent;


import com.switchfully.eurder.ItemComponent.IItemService;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.ItemGroupDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import com.switchfully.eurder.zExceptions.IllegalAmountException;
import com.switchfully.eurder.zExceptions.MandatoryFieldException;
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

    private void verifyOrder(CreateOrderDTO createOrderDTO){
        if (createOrderDTO == null) {
            throw new MandatoryFieldException("Please provide an order");
        }

        for (ItemGroupDTO itemGroup :
                createOrderDTO.getOrders()) {
            if (itemGroup.getId() == null) {
                throw new MandatoryFieldException("Please provide an id for each order");
            }
        }

        boolean amountOrderedBiggerThanAmountOfItems = createOrderDTO.getOrders().stream()
                .anyMatch(itemGroupDTO -> itemGroupDTO.getAmountOrdered() > itemService.getItemByName(itemGroupDTO.getId()).getAmount());
        if (amountOrderedBiggerThanAmountOfItems){
            throw new IllegalAmountException("Not enough items in stock");
        }

        boolean negativeAmount = createOrderDTO.getOrders().stream()
                .anyMatch(itemGroupDTO -> itemGroupDTO.getAmountOrdered() <= 0);
        if (negativeAmount){
            throw new IllegalAmountException("Please provide an amount bigger than 0");
        }
    }

    private List<Order> createOrders(CreateOrderDTO createOrderDTO){
        List<Order> result = new ArrayList<>();

        for (ItemGroupDTO itemGroupDTO :
                createOrderDTO.getOrders()) {
            ItemDTO itemFromOrder = itemService.getItemByName(itemGroupDTO.getId());
            result.add(new Order(itemFromOrder, itemGroupDTO.getAmountOrdered()));
        }

        return result;
    }

}
