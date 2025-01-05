package com.nvlhnn.warehouse.service.domain.entity;

import com.nvlhnn.domain.entity.AggregateRoot;
import com.nvlhnn.domain.valueobject.OrderId;
import com.nvlhnn.domain.valueobject.OrderStockMutationId;
import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.valueobject.StatusStockMutation;

public class OrderStockMutation extends AggregateRoot<OrderStockMutationId> {

    private final OrderId orderId;
    private final WarehouseId warehouseId;
    private final ProductId productId;
    private final int quantity;
    private StatusStockMutation statusStockMutation;

    private OrderStockMutation(Builder builder) {
        super.setId(builder.orderStockMutationId);
        this.orderId = builder.orderId;
        this.warehouseId = builder.warehouseId;
        this.productId = builder.productId;
        this.quantity = builder.quantity;
        this.statusStockMutation = builder.statusStockMutation;
    }

    public void validateStock() {

        if (this.statusStockMutation != StatusStockMutation.PENDING) {
            throw new IllegalStateException("Order stock mutation is not pending.");
        }
        if (this.quantity <= 0) {
            throw new IllegalStateException("Order stock mutation quantity must be greater than 0.");
        }
    }

    public void updateStatusStockMutation(StatusStockMutation statusStockMutation) {
        this.statusStockMutation = statusStockMutation;
    }

    public static Builder builder() {
        return new Builder();
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public WarehouseId getWarehouseId() {
        return warehouseId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public StatusStockMutation getStatusStockMutation() {
        return statusStockMutation;
    }

    public static final class Builder {
        private OrderStockMutationId orderStockMutationId;
        private OrderId orderId;
        private WarehouseId warehouseId;
        private ProductId productId;
        private int quantity;
        private StatusStockMutation statusStockMutation;

        private Builder() {
        }

        public Builder orderStockMutationId(OrderStockMutationId orderStockMutationId) {
            this.orderStockMutationId = orderStockMutationId;
            return this;
        }

        public Builder orderId(OrderId orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder warehouseId(WarehouseId warehouseId) {
            this.warehouseId = warehouseId;
            return this;
        }

        public Builder productId(ProductId productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder statusStockMutation(StatusStockMutation statusStockMutation) {
            this.statusStockMutation = statusStockMutation;
            return this;
        }

        public OrderStockMutation build() {
            return new OrderStockMutation(this);
        }
    }
}
