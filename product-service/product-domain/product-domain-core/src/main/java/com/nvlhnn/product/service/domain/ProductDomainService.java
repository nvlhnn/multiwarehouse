package com.nvlhnn.product.service.domain;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.product.service.domain.entity.Product;
import com.nvlhnn.product.service.domain.event.ProductCreatedEvent;

public interface ProductDomainService {

    ProductCreatedEvent initializeProduct(Product product, DomainEventPublisher<ProductCreatedEvent> publisher);

    void patchProduct(Product product, Integer totalStock);

}
