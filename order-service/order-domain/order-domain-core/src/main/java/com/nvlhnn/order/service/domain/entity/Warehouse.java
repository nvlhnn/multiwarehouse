package com.nvlhnn.order.service.domain.entity;

import com.nvlhnn.domain.entity.AggregateRoot;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.order.service.domain.exception.OrderDomainException;

import java.util.List;

public class Warehouse extends AggregateRoot<WarehouseId> {
    private final List<Product> products;
    private final String name;
    private boolean active;

    private Warehouse(Builder builder) {
        super.setId(builder.warehouseId);
        products = builder.products;
        name = builder.name;
        active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name;
    }

    public void validateInitializeWarehouse() {
        if (getId() == null) {
            throw new OrderDomainException("Id is null");
        }
        if (getName() == null) {
            throw new OrderDomainException("Name is null");
        }
    }

    public static final class Builder {
        private WarehouseId warehouseId;
        private List<Product> products;
        private String name;
        private boolean active;

        private Builder() {
        }

        public Builder warehouseId(WarehouseId val) {
            warehouseId = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Warehouse build() {
            return new Warehouse(this);
        }
    }
}
