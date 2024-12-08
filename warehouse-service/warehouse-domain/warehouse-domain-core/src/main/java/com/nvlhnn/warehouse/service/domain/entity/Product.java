package com.nvlhnn.warehouse.service.domain.entity;

import com.nvlhnn.domain.entity.AggregateRoot;
import com.nvlhnn.domain.valueobject.ProductId;

import java.math.BigDecimal;

public class Product extends AggregateRoot<ProductId> {

    private String name;
    private BigDecimal price;

    public Product(ProductId productId, String name, BigDecimal price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public  Product(ProductId productId){super.setId(productId);}

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
