package com.nvlhnn.order.service.domain.entity;

import com.nvlhnn.domain.entity.AggregateRoot;
import com.nvlhnn.domain.entity.BaseEntity;
import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.StockId;
import com.nvlhnn.domain.valueobject.WarehouseId;

import java.math.BigDecimal;

public class Stock extends BaseEntity<StockId> {

    private WarehouseId warehouseId;

    private ProductId productId;

    private int quantity;

    public Stock(StockId stockId, WarehouseId warehouseId, ProductId productId, int quantity) {
        super.setId(stockId);
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Stock(StockId stockId){
        super.setId(stockId);}


    public void updateQuantity(int quantity) {
        this.quantity = quantity;
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
}
