package com.nvlhnn.warehouse.service.domain.valueobject;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;

import java.util.Objects;

public class StockTransfer {

    private final WarehouseId fromWarehouseId;
    private final WarehouseId toWarehouseId;
    private final ProductId productId;
    private final StatusStockMutation statusStockMutation;
    private final int quantity;

    public StockTransfer(WarehouseId fromWarehouseId, WarehouseId toWarehouseId, ProductId productId, StatusStockMutation statusStockMutation, int quantity) {
        this.fromWarehouseId = fromWarehouseId;
        this.toWarehouseId = toWarehouseId;
        this.productId = productId;
        this.statusStockMutation = statusStockMutation;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockTransfer that = (StockTransfer) o;
        return quantity == that.quantity && Objects.equals(fromWarehouseId, that.fromWarehouseId) && Objects.equals(toWarehouseId, that.toWarehouseId) && Objects.equals(productId, that.productId) && statusStockMutation == that.statusStockMutation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromWarehouseId, toWarehouseId, productId, statusStockMutation, quantity);
    }

    public WarehouseId getFromWarehouseId() {
        return fromWarehouseId;
    }

    public WarehouseId getToWarehouseId() {
        return toWarehouseId;
    }

    public ProductId getProductId() {
        return productId;
    }

    public StatusStockMutation getStatusStockMutation() {
        return statusStockMutation;
    }

    public int getQuantity() {
        return quantity;
    }
}
