package com.nvlhnn.product.service.domain.event;

import com.nvlhnn.domain.event.DomainEvent;
import com.nvlhnn.product.service.domain.entity.Product;

import java.time.ZonedDateTime;

public abstract class ProductEvent implements DomainEvent<Product> {

    private final Product product;
    private final ZonedDateTime createdAt;

    public ProductEvent(Product product, ZonedDateTime createdAt) {
        this.product = product;
        this.createdAt = createdAt;
    }

    public Product getProduct() {
        return product;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    // Abstract method to be implemented by subclasses to fire the event
    public abstract void fire();
}
