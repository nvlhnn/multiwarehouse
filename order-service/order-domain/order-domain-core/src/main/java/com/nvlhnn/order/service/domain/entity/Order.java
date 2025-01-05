package com.nvlhnn.order.service.domain.entity;

import com.nvlhnn.domain.entity.AggregateRoot;
import com.nvlhnn.domain.valueobject.*;
import com.nvlhnn.order.service.domain.exception.OrderDomainException;
import com.nvlhnn.order.service.domain.valueobject.OrderItemId;
import com.nvlhnn.order.service.domain.valueobject.StreetAddress;
import com.nvlhnn.order.service.domain.valueobject.TrackingId;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class Order extends AggregateRoot<OrderId> {
    private final UserId userId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;

    private final Warehouse warehouse;

    private TrackingId trackingId;
    private OrderStatus orderStatus;

    private Date expiredAt;
    private List<String> failureMessages;

    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        expiredAt = Date.from(ZonedDateTime.now().plusDays(1).toInstant());
        initializeOrderItems();
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct state for pay operation!");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if(orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for approve operation!");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages) {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for initCancel operation!");
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(List<String> failureMessages) {
        if (!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)) {
            throw new OrderDomainException("Order is not in correct state for cancel operation!");
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    public void payOrder() {
        orderStatus = OrderStatus.PAID;
    }

    public void validatePayOrder(){
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct state for pay operation!");
        }

        if (expiredAt != null && expiredAt.before(new Date())) {
            throw new OrderDomainException("Order is expired!");
        }
    }

    public void cancelOrder() {
        orderStatus = OrderStatus.CANCELLED;
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
        }
        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }

    private void validateInitialOrder() {
        if (orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

//        if (!price.equals(orderItemsTotal)) {
//            throw new OrderDomainException("Total price: " + price.getAmount()
//                    + " is not equal to Order items total: " + orderItemsTotal.getAmount() + "!");
//        }
    }

    private void validateItemPrice(OrderItem orderItem) {
//        if (!orderItem.isPriceValid()) {
//            throw new OrderDomainException("Order item price: " + orderItem.getPrice().getAmount() +
//                    " is not valid for product " + orderItem.getProduct().getId().getValue());
//        }
    }


    public void validateOrderItems(List<Stock> stocks) {

        for (OrderItem orderItem: items) {
            // count total total stock with product id in orderItem
            int totalStock = stocks.stream()
                    .filter(stock -> stock.getProductId().equals(orderItem.getProduct().getId()))
                    .mapToInt(Stock::getQuantity)
                    .sum();
            if (orderItem.getQuantity() > totalStock) {
                throw new OrderDomainException("Order item quantity: " + orderItem.getQuantity() +
                        " is greater than total stock: " + totalStock + " for product " + orderItem.getProduct().getId().getValue());
            }

        }
    }

    public List<Stock> findAndTransferStock(WarehouseId nearestWarehouseId, List<Stock> stocks, ProductId productId, int requiredQuantity) {
        // List to store all stock objects that are reduced
        List<Stock> stockChanges = new java.util.ArrayList<>();

        // First, check the stock in the nearest warehouse
        Stock nearestWarehouseStock = findStockInWarehouse(nearestWarehouseId, productId, stocks);
        int stockInNearestWarehouse = nearestWarehouseStock != null ? nearestWarehouseStock.getQuantity() : 0;

        // If the nearest warehouse has enough stock, no need for transfer from others
        if (stockInNearestWarehouse >= requiredQuantity) {
            nearestWarehouseStock.updateQuantity(nearestWarehouseStock.getQuantity() - requiredQuantity);
            stockChanges.add(new Stock(nearestWarehouseStock.getId(), nearestWarehouseId, productId, -requiredQuantity));  // Add the reduced stock to list
            return stockChanges;
        }

        // If not enough stock in nearest warehouse, calculate remaining stock needed
        int remainingStockNeeded = requiredQuantity - stockInNearestWarehouse;

        // Add the reduced stock from nearest warehouse to stockChanges
        if (stockInNearestWarehouse > 0) {
            nearestWarehouseStock.updateQuantity(0); // Reducing all stock in nearest warehouse
            stockChanges.add(new Stock(nearestWarehouseStock.getId(), nearestWarehouseId, productId, -stockInNearestWarehouse));
        }

        // Gather available stocks from other warehouses (exclude the nearest warehouse)
        List<Stock> stocksToReduce = stocks.stream()
                .filter(stock -> stock.getProductId().equals(productId))  // Filter by product
                .filter(stock -> !stock.getWarehouseId().equals(nearestWarehouseId))  // Exclude nearest warehouse
                .filter(stock -> stock.getQuantity() > 0)  // Only consider warehouses with stock available
                .sorted((stock1, stock2) -> Integer.compare(stock2.getQuantity(), stock1.getQuantity()))  // Sort by available quantity (descending)
                .collect(Collectors.toList());

        // Loop through other warehouses to reduce stock until the remaining need is fulfilled
        for (Stock stock : stocksToReduce) {
            if (remainingStockNeeded <= 0) break; // Stop if no more stock is needed

            // Calculate how much stock can be taken from this warehouse
            int stockToReduce = Math.min(stock.getQuantity(), remainingStockNeeded);
            remainingStockNeeded -= stockToReduce;

            // Reduce the stock in this warehouse
            stock.updateQuantity(stock.getQuantity() - stockToReduce);
            stockChanges.add(new Stock(stock.getId(), stock.getWarehouseId(), productId, -stockToReduce));  // Add the reduced stock to the list
        }

        return stockChanges;  // Return the list of stock changes (with negative quantities)
    }

    private Stock findStockInWarehouse(WarehouseId warehouseId, ProductId productId, List<Stock> stocks) {
        return stocks.stream()
                .filter(stock -> stock.getWarehouseId().equals(warehouseId) && stock.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem orderItem: items) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    private Order(Builder builder) {
        super.setId(builder.orderId);
        userId = builder.userId;
        warehouse = builder.warehouse;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
        expiredAt = builder.expiredAt;

    }

    public static Builder builder() {
        return new Builder();
    }

    public UserId getUserId() {
        return userId;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public static final class Builder {
        private OrderId orderId;
        private UserId userId;
        private Warehouse warehouse;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;
        private Date expiredAt;

        private Builder() {
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder userId(UserId val) {
            userId = val;
            return this;
        }

        public Builder warehouse(Warehouse val) {
            warehouse = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Builder expiredAt(Date val) {
            expiredAt = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}