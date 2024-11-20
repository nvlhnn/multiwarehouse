package com.nvlhnn.warehouse.service.dataaccess.product.mapper;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.warehouse.service.dataaccess.product.entity.ProductEntity;
import com.nvlhnn.warehouse.service.domain.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDataAccessMapper {

    public ProductEntity productToProductEntityDataMapper(Product product) {
        return ProductEntity.builder()
                .id(product.getId().getValue())
                .name(product.getName())
                .build();
    }

    public Product productEntityToProductDataMapper(ProductEntity productEntity) {
        return new Product(
                new ProductId(productEntity.getId()),
                productEntity.getName()
        );

    }
}
