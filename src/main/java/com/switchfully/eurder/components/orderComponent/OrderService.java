package com.switchfully.eurder.components.orderComponent;


import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.itemComponent.IItemService;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.ItemGroupDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import com.switchfully.eurder.exception.*;
import com.switchfully.eurder.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final IItemService itemService;
    private final ICustomerService customerService;
    private final Utils utils;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, IItemService itemService, ICustomerService customerService){
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.itemService = itemService;
        this.customerService = customerService;
        this.utils = new Utils();
    }

    @Override
    public List<OrderDTO> orderItems(CreateOrderDTO createOrderDTO, String auth){

        CustomerDTO customerWhoOrdered = customerService.getCustomerFromAuth(auth);

        verifyOrder(createOrderDTO);

        List<Order> orders = createOrders(createOrderDTO,customerWhoOrdered);

        orderRepository.addOrder(orders);

        return orderMapper.mapToDTO(orders);
    }

    @Override
    public String reportOrdersByCustomer(String auth){

        CustomerDTO customer = customerService.getCustomerFromAuth(auth);

        return makeReportForCustomer(customer);

    }


    @Override
    public OrderDTO reorderExistingOrder(UUID orderId, String auth){

        CustomerDTO customerFromAuth = customerService.getCustomerFromAuth(auth);
        Order orderFromId = getOrderFromId(orderId);

        //TODO:

//        if (orderFromId.getCustomer().getId() != customerId){
//            throw new IllegalOrderException("This is not your order");
//        }

        Order updatedOrder = new Order(itemService.getItemById(orderFromId.getItem().getId()),
                orderFromId.getAmountOrdered(),
                customerService.getCustomerById(customerFromAuth.getId().toString()));

        orderRepository.addOrder(updatedOrder);

        return orderMapper.mapToDTO(updatedOrder);
    }

    @Override
    public String getShippingList(){
        String shippingList = "";

        for (Order order :
                orderRepository.getOrders()) {
            if (isShippingDateToday(order.getShippingDate())){
                shippingList += "\nItem to be shipped: \n" + order.getItem() + "\nShipped to: " + order.getCustomer().getAddress();
            }
        }

        //TODO: figure out how to do this in a stream

//        return orderRepository.getOrders().stream()
//                .filter(order -> isShippingDateToday(order.getShippingDate()))
//                .findAny()
//                .orElseThrow();

        return shippingList;
    }


    private String makeReportForCustomer(CustomerDTO customer){

        String report ="";

        double totalPriceForCustomer = 0;

        for (Order order :
                orderRepository.getOrders()) {
            if (order.getCustomer().getId().equals(customer.getId())){
                report += "The Id of the order: " + order.getId() +
                        "\nThis order contains the following Item: " + order.getItem().getName() + "\nYou ordered this amount: " + order.getAmountOrdered()
                        + "\nThe total price of this order contains: " + order.getTotalPrice() +"\n";
                totalPriceForCustomer += order.getTotalPrice();
            }
        }

        return report + "\nThe total amount of your orders is: " + totalPriceForCustomer;
    }

    private static boolean isShippingDateToday(LocalDate shippingDate){
        LocalDate today = LocalDate.now();

        return today.isEqual(shippingDate);
    }


    //TODO: fix access mistake in tests
    private void verifyOrder(CreateOrderDTO createOrderDTO){
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

    private Order getOrderFromId(UUID orderId){
        return orderRepository.getOrders().stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalIdException("Please provide a valid order ID"));
    }

    private void updateItemFromOrder(ItemGroupDTO itemGroupDTO){
        ItemDTO itemFromOrder = itemService.getItemById(itemGroupDTO.getId());
        int newItemAmount = itemFromOrder.getAmount() - itemGroupDTO.getAmountOrdered();
        UpdateItemDTO updatedItemDTO = new UpdateItemDTO(itemFromOrder.getName(),itemFromOrder.getDescription(),itemFromOrder.getPrice(),newItemAmount);
        itemService.updateItemById(updatedItemDTO, itemGroupDTO.getId());
    }

}
