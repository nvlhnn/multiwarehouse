package com.nvlhnn.warehouse.service.domain.entity;

import com.nvlhnn.domain.entity.BaseEntity;
import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.StockId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseDomainException;
import com.nvlhnn.warehouse.service.domain.valueobject.StatusStockMutation;
import com.nvlhnn.warehouse.service.domain.valueobject.StockJournal;
import com.nvlhnn.warehouse.service.domain.valueobject.StockTransfer;

import java.util.List;
import java.util.UUID;

public class Stock extends BaseEntity<StockId> {

    private WarehouseId warehouseId;

    private ProductId productId;

    private int quantity;

    private List<StockJournal> stockJournals;

    private List<StockTransfer> stockTransfers;


    private Stock(Builder builder) {
        super.setId(builder.stockId);
        warehouseId = builder.warehouseId;
        productId = builder.productId;
        quantity = builder.quantity;
        stockJournals = builder.stockJournals;
        stockTransfers = builder.stockTransfers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void initializeStock(WarehouseId warehouseId, ProductId productId){
        setId(new StockId(UUID.randomUUID()));
        this.warehouseId = warehouseId;
        this.productId = productId;
    }

    public void validateStock(boolean isNewStock) {
        if (getId() == null && !isNewStock) {
            throw new WarehouseDomainException("Stock is not in a valid state for initialization.");
        }

        if (getId() != null && isNewStock) {
            throw new WarehouseDomainException("Stock is already initialized.");
        }

        if (productId == null) {
            throw new WarehouseDomainException("Stock must be associated with a valid product.");
        }
        if (quantity < 0) {
            throw new WarehouseDomainException("Stock quantity must be greater than zero.");
        }
    }

    public void deductStock(int quantity) {
        if (this.quantity < quantity) {
            throw new WarehouseDomainException("Not enough stock to deduct.");
        }
        this.quantity = this.getQuantity() - quantity;
        recordStockJournal(quantity);
    }

    public void addStock(int quantity) {
        this.quantity = this.getQuantity() + quantity;
        recordStockJournal(quantity);
    }

    public void updateQuantity(int quantity) {
        int updatedQuantity = this.quantity + quantity;
        if (updatedQuantity < 0) {
            throw new WarehouseDomainException("Insufficient stock to perform the operation.");
        }
        this.quantity = updatedQuantity;
    }


    private void recordStockJournal(int quantity) {
        StockJournal journalEntry = new StockJournal(
                UUID.randomUUID(),
                this.warehouseId,
                this.productId,
                quantity
        );
        stockJournals.add(journalEntry);
    }

    public void recordStockTransfer(WarehouseId fromWarehouseId, WarehouseId toWarehouseId, ProductId productId, int quantity, String status) {
        StockTransfer transferEntry = new StockTransfer(
                fromWarehouseId,
                toWarehouseId,
                productId,
                StatusStockMutation.valueOf(status),
                quantity
        );
        stockTransfers.add(transferEntry);
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

    public List<StockJournal> getStockJournals() {
        return stockJournals;
    }

    public List<StockTransfer> getStockTransfers() {
        return stockTransfers;
    }

    public static final class Builder {
        private StockId stockId;
        private WarehouseId warehouseId;
        private ProductId productId;
        private int quantity;
        private List<StockJournal> stockJournals;
        private List<StockTransfer> stockTransfers;

        private Builder() {
        }

        public Builder stockId(StockId stockId) {
            this.stockId = stockId;
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

        public Builder stockJournals(List<StockJournal> stockJournals) {
            this.stockJournals = stockJournals;
            return this;
        }

        public Builder stockTransfers(List<StockTransfer> stockTransfers) {
            this.stockTransfers = stockTransfers;
            return this;
        }

        public Stock build() {
            return new Stock(this);
        }
    }
}
