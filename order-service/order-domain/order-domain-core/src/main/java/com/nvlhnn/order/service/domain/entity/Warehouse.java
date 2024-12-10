package com.nvlhnn.order.service.domain.entity;

import com.nvlhnn.domain.entity.AggregateRoot;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.order.service.domain.exception.OrderDomainException;

import java.util.List;

public class Warehouse extends AggregateRoot<WarehouseId> {
    private final List<Product> products;
    private final String name;
    private String city;
    private Double latitude;
    private Double longitude;
    private boolean active;


    private Warehouse(Builder builder) {
        super.setId(builder.warehouseId);
        products = builder.products;
        name = builder.name;
        city = builder.city;
        latitude = builder.latitude;
        longitude = builder.longitude;
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

    public String getCity() {
        return city;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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
        private String city;
        private double latitude;
        private double longitude;
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

        public Builder city(String val) {
            city = val;
            return this;
        }
        public Builder latitude(double val) {
            latitude = val;
            return this;
        }

        public Builder longitude(double val) {
            longitude = val;
            return this;
        }

        public Warehouse build() {
            return new Warehouse(this);
        }
    }
}
