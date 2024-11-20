package com.nvlhnn.warehouse.service.domain.entity;

import com.nvlhnn.domain.entity.AggregateRoot;
import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.StockId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseDomainException;
import com.nvlhnn.warehouse.service.domain.valueobject.StatusStockMutation;
import com.nvlhnn.warehouse.service.domain.valueobject.StockJournal;
import com.nvlhnn.warehouse.service.domain.valueobject.StockTransfer;
import com.nvlhnn.warehouse.service.domain.valueobject.StreetAddress;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Warehouse extends AggregateRoot<WarehouseId> {

    private String name;
    private StreetAddress streetAddress;
    private boolean isActive;
    private List<User> users;
    private List<Stock> stocks;


    private Warehouse(Builder builder) {
        super.setId(builder.warehouseId);
        name = builder.name;
        streetAddress = builder.streetAddress;
        isActive = builder.isActive;
        users = builder.users;
        stocks = builder.stocks;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void initializeWarehouse(){
        setId(new WarehouseId(UUID.randomUUID()));
        isActive = true;
        
    }

    public void validateInitialWarehouse() {
        validateInitialWarehouseState();
        validateName();
        validateAddress();
    }

    public void validateWarehouse(){
        if (getId() == null) {
            throw new WarehouseDomainException("Warehouse is not valid.");
        }
    }

    public void updateWarehouse(String name, StreetAddress streetAddress){
        this.name = name;
        this.streetAddress = streetAddress;
    }

    public void initDelete() {
        if (!isActive) {
            throw new WarehouseDomainException("Warehouse is not in correct state for initDelete operation!");
        }
        isActive = false;
    }

    public void addAssignedAdmin(User user) {
        if (!user.isWarehouseAdmin()) {
            throw new WarehouseDomainException("Only Warehouse Admins can be assigned.");
        }
        users.add(user);
    }

    private void validateInitialWarehouseState() {
        if (getId() != null) {
            throw new WarehouseDomainException("Warehouse is not in a valid state for initialization.");
        }
    }

    public boolean hasSufficientStock(ProductId productId, int quantity) {
        Optional<Stock> stock = findStockByProductId(productId);
        if (!stock.isPresent()){
            throw new WarehouseDomainException("Stock not found in warehouse.");
        }
        return stock.get().getQuantity() >= quantity;
    }

//    public void addOrCreateStock(ProductId productId, int quantity) {
//        Optional<Stock> stock = findStockByProductId(productId);
//        if (stock.isPresent()) {
//            stock.get().addStock(quantity);
//        } else {
//            initializeStock(productId, quantity);
//        }
//    }


    private Optional<Stock> findStockByProductId(ProductId productId) {
        return stocks.stream()
                .filter(stock -> stock.getProductId().equals(productId))
                .findFirst();
    }

    public void initializeStock(ProductId productId, int quantity) {
        Stock newStock = Stock.builder()
                .stockId(new StockId(UUID.randomUUID()))
                .quantity(quantity)
                .build();

        newStock.initializeStock(new WarehouseId(this.getId().getValue()), new ProductId(productId.getValue()));
        stocks.add(newStock);
    }

    private void validateName() {
        if (name == null || name.trim().isEmpty()) {
            throw new WarehouseDomainException("Warehouse name cannot be null or empty.");
        }
    }

    private void validateAddress() {
        if (streetAddress == null) {
            throw new WarehouseDomainException("Warehouse address must be provided.");
        }
        if (!streetAddress.isValid()) {
            throw new WarehouseDomainException("Warehouse address is incomplete or invalid.");
        }
    }


    public String getName() {
        return name;
    }

    public StreetAddress getStreetAddress() {
        return streetAddress;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public static final class Builder {
        private WarehouseId warehouseId;
        private String name;
        private StreetAddress streetAddress;
        private boolean isActive;
        private List<User> users;
        private List<Stock> stocks;


        private Builder() {
        }

        public Builder warehouseId(WarehouseId warehouseId) {
            this.warehouseId = warehouseId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder streetAddress(StreetAddress streetAddress) {
            this.streetAddress = streetAddress;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder users(List<User> users) {
            this.users = users;
            return this;
        }
        public Builder stocks(List<Stock> stocks) {
            this.stocks = stocks;
            return this;
        }

        public Warehouse build() {
            return new Warehouse(this);
        }
    }
}
