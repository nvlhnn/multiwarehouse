package com.nvlhnn.warehouse.service.domain.ports.output.repository;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.warehouse.service.domain.entity.Product;

import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(ProductId productId);

    Product save(Product product);

    void delete(Product product);
}
