package com.nvlhnn.product.service.dataaccess.product.adapter;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.product.service.dataaccess.product.mapper.ProductDataAccessMapper;
import com.nvlhnn.product.service.dataaccess.product.repository.ProductJpaRepository;
import com.nvlhnn.product.service.domain.entity.Product;
import com.nvlhnn.product.service.domain.ports.output.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final ProductDataAccessMapper productDataAccessMapper;

    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository, ProductDataAccessMapper productDataAccessMapper) {
        this.productJpaRepository = productJpaRepository;
        this.productDataAccessMapper = productDataAccessMapper;
    }

    @Override
    public Product save(Product product) {
        return productDataAccessMapper.productEntityToProduct(productJpaRepository
                .save(productDataAccessMapper.productToProductEntity(product)));
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return productJpaRepository.findById(productId.getValue())
                .map(productDataAccessMapper::productEntityToProduct);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productJpaRepository.findAll(pageable)
                .map(productDataAccessMapper::productEntityToProduct);
    }

}
