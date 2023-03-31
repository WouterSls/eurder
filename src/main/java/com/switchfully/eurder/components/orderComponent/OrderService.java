package com.switchfully.eurder.components.orderComponent;


import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.itemComponent.IItemService;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.ItemGroupDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import com.switchfully.eurder.components.securityComponent.ISecurityService;
import com.switchfully.eurder.exception.*;
import com.switchfully.eurder.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final IItemService itemService;
    private final ISecurityService securityService;
    private final ICustomerService customerService;
    private final Utils utils;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, IItemService itemService, ISecurityService securityService, ICustomerService customerService){
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.itemService = itemService;
        this.securityService = securityService;
        this.customerService = customerService;
        this.utils = new Utils();
    }

    @Override
    public List<OrderDTO> orderItems(CreateOrderDTO createOrderDTO, String auth){

        UUID customerIdFromAuth = securityService.getCustomerUUIDFromAuth(auth);
        if (customerIdFromAuth == null){
            throw new MandatoryFieldException("Please provide a valid customer ID");
        }
        CustomerDTO customerWhoOrdered = customerService.getCustomerById(customerIdFromAuth.toString());

        verifyUUID(customerWhoOrdered.getId());

        verifyOrder(createOrderDTO);
        List<Order> orders = createOrders(createOrderDTO,customerWhoOrdered);
        orderRepository.addOrders(orders);
        return orderMapper.mapToDTO(orders);
    }

    @Override
    public String reportOrdersByCustomer(String auth){

        if (auth == null){
            throw new MandatoryFieldException("Please provide a CustomerID");
        }

        UUID customerId = securityService.getCustomerUUIDFromAuth(auth);

        if (customerId == null){
            throw new InvalidIdFormatException("Please provide a correct CustomerID");
        }

        verifyUUID(customerId);

        String report ="";
        double totalPriceForCustomer = 0;

        for (Order order :
                orderRepository.getOrders()) {
            if (order.getCustomer().getId().equals(customerId)){
                report += "The Id of the order: " + order.getId() +
                        "\nThis order contains the following Item: " + order.getItem().getName() + "\nYou ordered this amount: " + order.getAmountOrdered()
                        + "\nThe total price of this order contains: " + order.getTotalPrice() +"\n";
                totalPriceForCustomer += order.getTotalPrice();
            }
        }


        return report + "\nThe total amount of your orders is: " + totalPriceForCustomer;
    }


    @Override
    public OrderDTO reorderExistingOrder(UUID orderId, String auth){
        if (orderId == null){
            throw new MandatoryFieldException("Please provide a orderID");
        }
        UUID customerId = securityService.getCustomerUUIDFromAuth(auth);
        Order orderFromId = getOrderFromId(orderId).orElseThrow(() -> new IllegalIdException("Please provide a valid orderID"));

        //TODO:

//        if (orderFromId.getCustomer().getId() != customerId){
//            throw new IllegalOrderException("This is not your order");
//        }

        Order updateOrder = new Order(itemService.getItemById(orderFromId.getItem().getId().toString()),
                orderFromId.getAmountOrdered(),
                customerService.getCustomerById(customerId.toString()));

        orderRepository.addOrders(List.of(updateOrder));

        return orderMapper.mapToDTO(updateOrder);
    }

    public OrderDTO getOrderById(UUID id){
        return orderMapper.mapToDTO(orderRepository.getOrders().stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElse(null));
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

    private Optional<Order> getOrderFromId(UUID orderId){
        return orderRepository.getOrders().stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst();
    }

    private List<Order> createOrders(CreateOrderDTO createOrderDTO, CustomerDTO customer){
        List<Order> result = new ArrayList<>();

        for (ItemGroupDTO itemGroupDTO :
                createOrderDTO.getOrders()) {

            Order createdOrder = createOrder(itemGroupDTO,customer);
            result.add(createdOrder);

            updateItemFromOrder(itemGroupDTO);
        }

        return result;
    }

    private Order createOrder(ItemGroupDTO itemGroupDTO, CustomerDTO customer){
        ItemDTO itemFromOrder = itemService.getItemById(itemGroupDTO.getId());
        return new Order(itemFromOrder, itemGroupDTO.getAmountOrdered(),customer);
    }

    private void updateItemFromOrder(ItemGroupDTO itemGroupDTO){
        ItemDTO itemFromOrder = itemService.getItemById(itemGroupDTO.getId());
        int newItemAmount = itemFromOrder.getAmount() - itemGroupDTO.getAmountOrdered();
        UpdateItemDTO updatedItemDTO = new UpdateItemDTO(itemFromOrder.getName(),itemFromOrder.getDescription(),itemFromOrder.getPrice(),newItemAmount);
        itemService.updateItemById(updatedItemDTO, itemGroupDTO.getId());
    }

    private void verifyUUID(UUID id){
        if (id == null){
            throw new MandatoryFieldException("Please provide a correct Customer UUID");
        }
        if (!utils.isValidUUIDFormat(id.toString())){
            throw new InvalidIdFormatException("Please provide a valid UUID format");
        }
    }
}
