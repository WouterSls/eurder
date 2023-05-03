package com.switchfully.eurder.components.orderComponent;


import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.itemGroup.CreateItemGroupDTO;
import com.switchfully.eurder.components.customerComponent.Customer;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.itemComponent.IItemService;
import com.switchfully.eurder.components.itemComponent.Item;
import com.switchfully.eurder.exception.IllegalAmountException;
import com.switchfully.eurder.exception.IllegalIdException;
import com.switchfully.eurder.exception.IllegalOrderException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final IItemService itemService;
    private final ICustomerService customerService;

    @Autowired
    public OrderService(IOrderRepository orderRepository, OrderMapper orderMapper, IItemService itemService, ICustomerService customerService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.itemService = itemService;
        this.customerService = customerService;
    }

    @Override
    public Order orderItems(CreateOrderDTO createOrderDTO, Jwt jwt) {

        verifyOrder(createOrderDTO);

        Customer customerWhoOrdered = customerService.getCustomerFromAuth(jwt);

        List<ItemGroup> itemsToOrder = makeOrder(createOrderDTO);

        double totalPrice = calculateTotalPrice(itemsToOrder);

        Order newOrder = orderMapper.mapToDomain(itemsToOrder,customerWhoOrdered,totalPrice);

        itemsToOrder
                .forEach(this::updateItemFromOrder);

        itemsToOrder.
                forEach(itemGroup -> itemGroup.setFk_order(newOrder));

        return orderRepository.save(newOrder);
    }

    @Override
    public Order reorderExistingOrder(UUID orderId, Jwt jwt) {

        Customer customerFromAuth = customerService.getCustomerFromAuth(jwt);

        Order existingOrder = getOrderFromId(orderId);

        if (existingOrder.getCustomer().getId() != customerFromAuth.getId()) {
            throw new IllegalOrderException("This is not your order");
        }

        Order reOrder = orderMapper.mapToDomain(existingOrder.getItemGroups(),customerFromAuth,calculateTotalPrice(existingOrder.getItemGroups()));
        return orderRepository.save(reOrder);
    }

    @Override
    public String reportOrdersByCustomer(Jwt jwt) {

        Customer customer = customerService.getCustomerFromAuth(jwt);

        return makeReportForCustomer(customer);
    }

    @Override
    public String getShippingList(){
        return "test";
    }

    @Override
    public List<Order> findAllOrders(){
        return orderRepository.findAll();
    }
    private String makeReportForCustomer(Customer customer){
        return orderRepository.findAll().stream()
                .filter(order -> order.getCustomer().equals(customer))
                .toString();
        }

    private Order getOrderFromId(UUID id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("provide valid order id"));
    }

    private List<ItemGroup> makeOrder(CreateOrderDTO createOrderDTO){
        return createOrderDTO.getItemsToOrder().stream()
                .map(this::createItemGroup)
                .toList();
    }
    private ItemGroup createItemGroup(CreateItemGroupDTO createItemGroupDTO){
        Item itemToOrder = itemService.findById(createItemGroupDTO.getId());
        return new ItemGroup(itemToOrder,
                createItemGroupDTO.getAmountOrdered(),
                calculateShippingDate(itemToOrder, createItemGroupDTO));
    }

    private LocalDate calculateShippingDate(Item item, CreateItemGroupDTO createItemGroupDTO){
        if (item.getAmount() > createItemGroupDTO.getAmountOrdered()){
            return LocalDate.now();
        }
        return LocalDate.now().plusWeeks(1);
    }

    private static boolean isShippingDateToday(LocalDate shippingDate) {
        LocalDate today = LocalDate.now();

        return today.isEqual(shippingDate);
    }

    private void verifyOrder(CreateOrderDTO createOrderDTO) {
        if (createOrderDTO == null) {
            throw new MandatoryFieldException("No order provided");
        }
        if (createOrderDTO.getItemsToOrder() == null || createOrderDTO.getItemsToOrder().isEmpty()) {
            throw new MandatoryFieldException("Please provide at least one ItemGroup");
        }

        for (CreateItemGroupDTO itemGroup :
                createOrderDTO.getItemsToOrder()) {
            if (itemGroup.getId() == null) {
                throw new MandatoryFieldException("Provide an id for each order for all items");
            }
        }

        for (CreateItemGroupDTO itemGroup :
                createOrderDTO.getItemsToOrder()) {
            if (itemGroup.getAmountOrdered() <= 0) {
                throw new IllegalAmountException("Provide an amount bigger than 0 for all items");
            }
        }

        for (CreateItemGroupDTO itemGroup :
                createOrderDTO.getItemsToOrder()) {
            if (itemService.findById(itemGroup.getId()) == null) {
                throw new IllegalIdException("No item found for provided ID");
            }
        }

        boolean amountOrderedBiggerThanAmountOfItems = createOrderDTO.getItemsToOrder().stream()
                .anyMatch(itemGroupDTO -> itemGroupDTO.getAmountOrdered() > itemService.findById(itemGroupDTO.getId()).getAmount());
        if (amountOrderedBiggerThanAmountOfItems) {
            throw new IllegalAmountException("Not enough items in stock");
        }

    }

    private double calculateTotalPrice(List<ItemGroup> orderedItems){
        return orderedItems.stream()
                .mapToDouble(itemGroup -> itemGroup.getAmountOrdered() * itemGroup.getItem().getPrice())
                .sum();
    }




    private void updateItemFromOrder(ItemGroup itemGroup) {
        Item itemFromItemGroup = itemService.findById(itemGroup.getItem().getId());
        int newItemAmount = itemFromItemGroup.getAmount() - itemGroup.getAmountOrdered();
        UpdateItemDTO updatedItemDTO = new UpdateItemDTO(itemFromItemGroup.getName(), itemFromItemGroup.getDescription(), itemFromItemGroup.getPrice(), newItemAmount);
        itemService.updateItemById(updatedItemDTO, itemGroup.getItem().getId());
    }

}
