package com.nvlhnn.order.service.domain;

import com.nvlhnn.domain.valueobject.StockId;
import com.nvlhnn.order.service.domain.dto.create.CreateOrderCommand;
import com.nvlhnn.order.service.domain.dto.create.CreateOrderResponse;
import com.nvlhnn.order.service.domain.dto.create.OrderAddress;
import com.nvlhnn.order.service.domain.dto.create.OrderItem;
import com.nvlhnn.order.service.domain.dto.track.TrackOrderQuery;
import com.nvlhnn.order.service.domain.dto.track.TrackOrderResponse;
import com.nvlhnn.order.service.domain.dto.create.OrderListResponse;
import com.nvlhnn.order.service.domain.dto.create.OrderStatsResponse;
import com.nvlhnn.order.service.domain.entity.Order;
import com.nvlhnn.order.service.domain.entity.User;
import com.nvlhnn.order.service.domain.entity.Warehouse;
import com.nvlhnn.order.service.domain.event.OrderCreatedEvent;
import com.nvlhnn.order.service.domain.exception.OrderDomainException;
import com.nvlhnn.order.service.domain.mapper.OrderDataMapper;
import com.nvlhnn.order.service.domain.ports.input.service.OrderApplicationService;
import com.nvlhnn.order.service.domain.ports.output.repository.*;
import com.nvlhnn.order.service.domain.ports.output.message.publisher.OrderCreatedPaymentRequestMessagePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the OrderApplicationService.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WarehouseRepository warehouseRepository;

    @MockBean
    private StockRepository stockRepository;

    @MockBean
    private OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    @Autowired
    private OrderDataMapper orderDataMapper;



    private CreateOrderCommand createOrderCommand;
    private TrackOrderQuery trackOrderQuery;

    private final UUID USER_ID = UUID.randomUUID();
    private final String USER_EMAIL = "john.doe@example.com";
    private final String USER_NAME = "John Doe";
    private final String WAREHOUSE_ID = UUID.randomUUID().toString();
    private final String PRODUCT_ID = UUID.randomUUID().toString();
    private final BigDecimal PRODUCT_PRICE = new BigDecimal("100.00");
    private final int PRODUCT_QUANTITY = 2;

    @BeforeEach
    public void init() {
        // Initialize CreateOrderCommand for successful order creation
        OrderAddress address = OrderAddress.builder()
                .street("123 Main St")
                .postalCode("12345")
                .city("Metropolis")
                .latitude(40.7128)
                .longitude(-74.0060)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .productId(UUID.fromString(PRODUCT_ID))
                .quantity(PRODUCT_QUANTITY)
                .price(PRODUCT_PRICE)
                .subTotal(PRODUCT_PRICE.multiply(new BigDecimal(PRODUCT_QUANTITY)))
                .build();

        createOrderCommand = CreateOrderCommand.builder()
                .price(PRODUCT_PRICE.multiply(new BigDecimal(PRODUCT_QUANTITY)))
                .items(List.of(orderItem))
                .address(address)
                .userId(USER_ID)
                .build();

        // Initialize TrackOrderQuery
        trackOrderQuery = TrackOrderQuery.builder()
                .orderTrackingId(UUID.randomUUID())
                .build();

        // Mock PasswordEncoder behavior used by OrderDomainServiceImpl

        // Mock userRepository.findById for successful order creation
        User user = User.builder()
                .userId(new com.nvlhnn.domain.valueobject.UserId(USER_ID))
                .name(USER_NAME)
                .email(USER_EMAIL)
                .role(com.nvlhnn.domain.valueobject.UserRole.CUSTOMER)
                .isActive(true)
                .token("jwtToken")
                .build();
        user.initializeUser();
        when(userRepository.findById(new com.nvlhnn.domain.valueobject.UserId(USER_ID)))
                .thenReturn(Optional.of(user));

        // Mock warehouseRepository.findNearestLocation
        Warehouse warehouse = Warehouse.builder()
                .warehouseId(new com.nvlhnn.domain.valueobject.WarehouseId(UUID.fromString(WAREHOUSE_ID)))
                .name("Central Warehouse")
                .city("Metropolis")
                .latitude(40.7128)
                .longitude(-74.0060)
                .active(true)
                .products(List.of())
                .build();
        when(warehouseRepository.findNearestLocation(anyDouble(), anyDouble()))
                .thenReturn(Optional.of(warehouse));

        // Mock stockRepository.findByProductIdIn
        com.nvlhnn.order.service.domain.entity.Stock stock = new com.nvlhnn.order.service.domain.entity.Stock(
                new StockId(UUID.randomUUID()),
                new com.nvlhnn.domain.valueobject.WarehouseId(UUID.fromString(WAREHOUSE_ID)),
                new com.nvlhnn.domain.valueobject.ProductId(UUID.fromString(PRODUCT_ID)),
                10 // Available stock
        );
        when(stockRepository.findByProductIdIn(anyList()))
                .thenReturn(Optional.of(List.of(stock)));

        // Mock orderRepository.save to return the saved order
        Order order = Order.builder()
                .userId(user.getId())
                .warehouse(warehouse)
                .deliveryAddress(orderDataMapper.orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new com.nvlhnn.domain.valueobject.Money(createOrderCommand.getPrice()))
                .items(orderDataMapper.orderItemsToOrderItemEntities(createOrderCommand.getItems()))
                .build();
        order.initializeOrder();
        when(orderRepository.save(any(Order.class))).thenReturn(order);

//        when(request.getAttribute("userId")).thenReturn(USER_ID.toString());

    }

    @Test
    public void testCreateOrder_Success() {

        //mock httpServletRequest

        // Perform order creation
        CreateOrderResponse response = orderApplicationService.createOrder(createOrderCommand);

        // Assertions
        assertNotNull(response, "CreateOrderResponse should not be null");
        assertNotNull(response.getOrderId(), "Order ID should not be null");
        assertNotNull(response.getOrderTrackingId(), "Order Tracking ID should not be null");
        assertEquals(com.nvlhnn.domain.valueobject.OrderStatus.PENDING, response.getOrderStatus(), "Order status should be PENDING");
        assertEquals("Order created successfully", response.getMessage(), "Success message should match");

        // Verify that orderRepository.save was called once
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();
        assertEquals(new com.nvlhnn.domain.valueobject.UserId(USER_ID), capturedOrder.getUserId(), "User ID should match");
        assertEquals(new com.nvlhnn.domain.valueobject.WarehouseId(UUID.fromString(WAREHOUSE_ID)), capturedOrder.getWarehouse().getId(), "Warehouse ID should match");
        assertEquals(new com.nvlhnn.domain.valueobject.Money(PRODUCT_PRICE.multiply(new BigDecimal(PRODUCT_QUANTITY))), capturedOrder.getPrice(), "Total price should match");
        assertEquals(1, capturedOrder.getItems().size(), "There should be one order item");
        assertEquals(new com.nvlhnn.domain.valueobject.ProductId(UUID.fromString(PRODUCT_ID)), capturedOrder.getItems().get(0).getProduct().getId(), "Product ID should match");

        // Verify that OrderCreatedPaymentRequestMessagePublisher.publish was called once
        verify(orderCreatedPaymentRequestMessagePublisher, times(1)).publish(any(OrderCreatedEvent.class));
    }

    @Test
    public void testCreateOrder_UserNotFound() {
        // Mock userRepository.findById to return empty
        when(userRepository.findById(new com.nvlhnn.domain.valueobject.UserId(USER_ID)))
                .thenReturn(Optional.empty());

        // Attempt to create order and expect exception
        OrderDomainException exception = assertThrows(OrderDomainException.class, () -> {
            orderApplicationService.createOrder(createOrderCommand);
        });

        // Assertions
        assertEquals("Could not find user with user id: " + new com.nvlhnn.domain.valueobject.UserId(USER_ID).getValue(), exception.getMessage(), "Exception message should match");
        // Verify that orderRepository.save was never called
        verify(orderRepository, never()).save(any(Order.class));

        // Verify that OrderCreatedPaymentRequestMessagePublisher.publish was never called
        verify(orderCreatedPaymentRequestMessagePublisher, never()).publish(any(OrderCreatedEvent.class));
    }

    @Test
    public void testCreateOrder_InsufficientStock() {
        // Mock stockRepository.findByProductIdIn to return insufficient stock
        com.nvlhnn.order.service.domain.entity.Stock stock = new com.nvlhnn.order.service.domain.entity.Stock(
                new StockId(UUID.randomUUID()),
                new com.nvlhnn.domain.valueobject.WarehouseId(UUID.fromString(WAREHOUSE_ID)),
                new com.nvlhnn.domain.valueobject.ProductId(UUID.fromString(PRODUCT_ID)),
                1 // Available stock less than required
        );
        when(stockRepository.findByProductIdIn(anyList()))
                .thenReturn(Optional.of(List.of(stock)));

        // Attempt to create order and expect exception
        OrderDomainException exception = assertThrows(OrderDomainException.class, () -> {
            orderApplicationService.createOrder(createOrderCommand);
        });

        // Assertions
        assertEquals("Order item quantity: 2 is greater than total stock: 1 for product " + new com.nvlhnn.domain.valueobject.ProductId(UUID.fromString(PRODUCT_ID)).getValue(), exception.getMessage(), "Exception message should match");

        // Verify that orderRepository.save was never called
        verify(orderRepository, never()).save(any(Order.class));

        // Verify that OrderCreatedPaymentRequestMessagePublisher.publish was never called
        verify(orderCreatedPaymentRequestMessagePublisher, never()).publish(any(OrderCreatedEvent.class));
    }


}
