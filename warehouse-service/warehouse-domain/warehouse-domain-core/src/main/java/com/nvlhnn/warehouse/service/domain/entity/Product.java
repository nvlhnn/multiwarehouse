package com.nvlhnn.warehouse.service.domain.entity;

import com.nvlhnn.domain.entity.BaseEntity;
import com.nvlhnn.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {

    private String name;

    public Product(ProductId productId, String name) {
        super.setId(productId);
        this.name = name;
    }

    public  Product(ProductId productId){super.setId(productId);}

    public String getName() {
        return name;
    }
}
