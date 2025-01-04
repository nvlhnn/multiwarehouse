package com.nvlhnn.order.service.domain;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.UserId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.order.service.domain.dto.create.CreateOrderCommand;
import com.nvlhnn.order.service.domain.entity.*;
import com.nvlhnn.order.service.domain.event.*;
import com.nvlhnn.order.service.domain.exception.*;
import com.nvlhnn.order.service.domain.mapper.OrderDataMapper;
import com.nvlhnn.order.service.domain.ports.output.message.publisher.OrderCreatedPaymentRequestMessagePublisher;
import com.nvlhnn.order.service.domain.ports.output.repository.StockRepository;
import com.nvlhnn.order.service.domain.ports.output.repository.UserRepository;
import com.nvlhnn.order.service.domain.ports.output.repository.OrderRepository;
import com.nvlhnn.order.service.domain.ports.output.repository.WarehouseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateHelper {
    private final OrderDomainService orderDomainService;

    private final OrderRepository orderRepository;

    private final StockRepository stockRepository;

    private final UserRepository userRepository;

    private final WarehouseRepository warehouseRepository;

    private final OrderDataMapper orderDataMapper;

    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedEventDomainEventPublisher;

    public OrderCreateHelper(OrderDomainService orderDomainService,
                             StockRepository stockRepository,
                             OrderRepository orderRepository,
                             UserRepository userRepository,
                             WarehouseRepository warehouseRepository,
                             OrderDataMapper orderDataMapper,
                             OrderCreatedPaymentRequestMessagePublisher orderCreatedEventDomainEventPublisher) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.warehouseRepository = warehouseRepository;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedEventDomainEventPublisher = orderCreatedEventDomainEventPublisher;
    }

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {

        // check user
        Optional<User> optionalUser = userRepository.findById(new UserId(createOrderCommand.getUserId()));
        if (optionalUser.isEmpty()) {
            log.warn("Could not find user with user id: {}", createOrderCommand.getUserId().toString());
            throw new OrderDomainException("Could not find user with user id: " + createOrderCommand.getUserId().toString());
        }

        // get nearest warehouse
        Optional<Warehouse> nearestWarehouse = warehouseRepository.findNearestLocation(createOrderCommand.getAddress().getLatitude(), createOrderCommand.getAddress().getLongitude());
        if (nearestWarehouse.isEmpty()) {
            log.warn("Could not find nearest warehouse!");
            throw new OrderDomainException("Could not find nearest warehouse!");
        }

        List<ProductId> productIds = createOrderCommand.getItems().stream().map(orderItem -> new ProductId(orderItem.getProductId())).toList();
        Optional<List<Stock>> stocks = stockRepository.findByProductIdIn(productIds);
        if (stocks.isEmpty()) {
            log.warn("Could not find stocks for products: {}", productIds);
            throw new OrderDomainException("Could not find stocks for products: " + productIds);
        }

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand, nearestWarehouse.get(), optionalUser.get());
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, stocks.get(), nearestWarehouse.get(),
                orderCreatedEventDomainEventPublisher);
        saveOrder(order);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        return orderCreatedEvent;
    }

//    private Warehouse checkWarehouse(CreateOrderCommand createOrderCommand) {
//        Optional<Warehouse> optionalWarehouse = warehouseRepository.findById(new WarehouseId(createOrderCommand.getWarehouseId()));
//        if (optionalWarehouse.isEmpty()) {
//            log.warn("Could not find warehouse with warehouse id: {}", createOrderCommand.getWarehouseId());
//            throw new OrderDomainException("Could not find warehouse with warehouse id: " +
//                    createOrderCommand.getWarehouseId());
//        }
//        return optionalWarehouse.get();
//    }

//    private boolean isStockAvailable(ProductId productId, int quantity) {
//
//        Integer quantity = stockRepository.checkTotalProductStock(productId);
//
//    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if (orderResult == null) {
            log.error("Could not save order!");
            throw new OrderDomainException("Could not save order!");
        }
        log.info("Order is saved with id: {}", orderResult.getId().getValue());
        return orderResult;
    }
}
