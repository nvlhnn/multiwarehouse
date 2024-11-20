package com.nvlhnn.warehouse.service.domain.valueobject;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;

import java.util.Objects;
import java.util.UUID;

public class StockJournal {

    private final UUID id;

    private final WarehouseId warehouseId;

    private final ProductId productId;

    private final int quantity;

    public StockJournal(UUID id, WarehouseId warehouseId, ProductId productId, int quantity) {
        this.id = id;
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockJournal that = (StockJournal) o;
        return quantity == that.quantity && Objects.equals(id, that.id) && Objects.equals(warehouseId, that.warehouseId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, warehouseId, productId, quantity);
    }
}
