package com.nvlhnn.product.service.dataaccess.product.mapper;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.product.service.dataaccess.product.entity.ProductEntity;
import com.nvlhnn.product.service.domain.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDataAccessMapper {

    public ProductEntity productToProductEntity(Product product) {
        return ProductEntity.builder()
                .id(product.getId().getValue())
                .name(product.getName())
                .price(product.getPrice())
                .totalStock(product.getTotalStock())
                .build();
    }

    public Product productEntityToProduct(ProductEntity productEntity) {
        return Product.builder()
                .productId(new ProductId(productEntity.getId()))
                .name(productEntity.getName())
                .price(productEntity.getPrice())
                .totalStock(productEntity.getTotalStock())
                .build();
    }
}
