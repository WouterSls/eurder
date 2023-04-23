package com.switchfully.eurder.components.orderComponent;


import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.api.dto.order.CreateOrdersDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import com.switchfully.eurder.api.dto.order.itemGroup.OneOrderDTO;
import com.switchfully.eurder.components.customerComponent.Customer;
import com.switchfully.eurder.components.customerComponent.ICustomerRepository;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.itemComponent.IItemRepository;
import com.switchfully.eurder.components.itemComponent.IItemService;
import com.switchfully.eurder.components.itemComponent.Item;
import com.switchfully.eurder.exception.IllegalAmountException;
import com.switchfully.eurder.exception.IllegalIdException;
import com.switchfully.eurder.exception.IllegalOrderException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final IItemService itemService;
    private final IItemRepository itemRepository;
    private final ICustomerService customerService;
    private final ICustomerRepository customerRepository;

    @Autowired
    public OrderService(IOrderRepository orderRepository, OrderMapper orderMapper, IItemService itemService, ICustomerService customerService, ICustomerRepository customerRepository, IItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.itemService = itemService;
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public List<OrderDTO> orderItems(CreateOrdersDTO createOrdersDTO, Jwt jwt) {

        verifyOrder(createOrdersDTO);



        Customer customerWhoOrdered = customerRepository.findAll().stream()
                .filter(customer -> customer.getEmailAddress().equals(jwt.getClaim("email")))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("no customer for this auth"));

        List<Order> orders = createOrders(createOrdersDTO, customerWhoOrdered);

        orderRepository.saveAll(orders);

        return orderMapper.mapToDTO(orders);
    }

    @Override
    public String reportOrdersByCustomer(Jwt jwt) {

        Customer customer = customerRepository.findById(UUID.fromString(jwt.getId()))
                .orElseThrow(() -> new IllegalArgumentException("no customer exists for auth"));

        return makeReportForCustomer(customer);

    }


    @Override
    public OrderDTO reorderExistingOrder(UUID orderId, Jwt jwt) {

        Customer customerFromAuth = customerRepository.findAll().stream()
                .filter(customer -> customer.getEmailAddress().equals(jwt.getClaim("email")))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No customer in database linked to authentication"));

        Order existingOrder = getOrderFromId(orderId);

        if (existingOrder.getCustomer().getId() != customerFromAuth.getId()) {
            throw new IllegalOrderException("This is not your order");
        }

        Order updatedOrder = new Order(existingOrder.getItem(), existingOrder.getAmountOrdered(), customerFromAuth);

        orderRepository.save(updatedOrder);

        return orderMapper.mapToDTO(updatedOrder);
    }

    @Override
    public String getShippingList() {

        return orderRepository.findAll().stream()
                .filter(order -> isShippingDateToday(order.getShippingDate()))
                .map(order -> "Item to be shipped: " + order.getItem() + "\n\tShipped to: " + order.getCustomer().getAddress())
                .collect(Collectors.joining("\n"));
    }


    private String makeReportForCustomer(Customer customer) {

        String report = "";

        double totalPriceForCustomer = 0;

        for (Order order :
                orderRepository.findAll()) {
            if (order.getCustomer().getId().equals(customer.getId())) {
                report += "The Id of the order: " + order.getId() +
                        "\nThis order contains the following Item: " + order.getItem().getName() + "\nYou ordered this amount: " + order.getAmountOrdered()
                        + "\nThe total price of this order contains: " + order.getTotalPrice() + "\n";
                totalPriceForCustomer += order.getTotalPrice();
            }
        }

        return report + "\nThe total amount of your orders is: " + totalPriceForCustomer;
    }

    private static boolean isShippingDateToday(LocalDate shippingDate) {
        LocalDate today = LocalDate.now();

        return today.isEqual(shippingDate);
    }

    private void verifyOrder(CreateOrdersDTO createOrdersDTO) {
        if (createOrdersDTO == null) {
            throw new MandatoryFieldException("No order provided");
        }
        if (createOrdersDTO.getOrders() == null || createOrdersDTO.getOrders().isEmpty()) {
            throw new MandatoryFieldException("Please provide at least one ItemGroup");
        }

        for (OneOrderDTO itemGroup :
                createOrdersDTO.getOrders()) {
            if (itemGroup.getId() == null) {
                throw new MandatoryFieldException("Provide an id for each order for all items");
            }
        }

        for (OneOrderDTO itemGroup :
                createOrdersDTO.getOrders()) {
            if (itemGroup.getAmountOrdered() <= 0) {
                throw new IllegalAmountException("Provide an amount bigger than 0 for all items");
            }
        }

        for (OneOrderDTO itemGroup :
                createOrdersDTO.getOrders()) {
            if (itemService.getItemById(itemGroup.getId()) == null) {
                throw new IllegalIdException("No item found for provided ID");
            }
        }

        boolean amountOrderedBiggerThanAmountOfItems = createOrdersDTO.getOrders().stream()
                .anyMatch(itemGroupDTO -> itemGroupDTO.getAmountOrdered() > itemService.getItemById(itemGroupDTO.getId()).getAmount());
        if (amountOrderedBiggerThanAmountOfItems) {
            throw new IllegalAmountException("Not enough items in stock");
        }

    }

    private List<Order> createOrders(CreateOrdersDTO createOrdersDTO, Jwt jwt) {
        List<Order> result = new ArrayList<>();

        Customer customerWhoOrdered = customerRepository.findAll().stream()
                .filter(customer -> customer.getEmailAddress().equals(jwt.getClaim("email")))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No customer linked to JWT token"));

        List<OneOrderDTO> orders = createOrdersDTO.getOrders();

        List<Order> createdOrders = orders.stream()
                .map(oneOrderDTO -> itemRepository.findById(oneOrderDTO.getId()))

        for (OneOrderDTO oneOrderDTO :
                createOrdersDTO.getOrders()) {

            Item item = itemRepository.findById(oneOrderDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("No item found for provided item ID"));

            Order createdOrder = orderMapper.mapToDomain(item, oneOrderDTO.getAmountOrdered(), customerWhoOrdered);

            result.add(createdOrder);

            updateItemFromOrder(oneOrderDTO);
        }

        return result;
    }

    private Order createOrder(OneOrderDTO oneOrderDTO, Customer customer) {
        Item itemFromOrder = itemRepository.findById(oneOrderDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("provide an existing item"));

        return new Order(itemFromOrder, oneOrderDTO.getAmountOrdered(), customer);
    }

    private Order getOrderFromId(UUID orderId) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalIdException("Please provide a valid order ID"));
    }

    private void updateItemFromOrder(OneOrderDTO oneOrderDTO) {
        ItemDTO itemFromOrder = itemService.getItemById(oneOrderDTO.getId());
        int newItemAmount = itemFromOrder.getAmount() - oneOrderDTO.getAmountOrdered();
        UpdateItemDTO updatedItemDTO = new UpdateItemDTO(itemFromOrder.getName(), itemFromOrder.getDescription(), itemFromOrder.getPrice(), newItemAmount);
        itemService.updateItemById(updatedItemDTO, oneOrderDTO.getId());
    }

}
