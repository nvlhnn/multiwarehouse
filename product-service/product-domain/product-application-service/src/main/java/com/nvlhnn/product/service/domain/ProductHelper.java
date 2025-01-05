package com.nvlhnn.product.service.domain;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.product.service.domain.dto.post.CreateProductCommand;
import com.nvlhnn.product.service.domain.entity.Product;
import com.nvlhnn.product.service.domain.event.ProductCreatedEvent;
import com.nvlhnn.product.service.domain.exception.ProductDomainException;
import com.nvlhnn.product.service.domain.mapper.ProductDataMapper;
import com.nvlhnn.product.service.domain.ports.output.message.publisher.ProductCreatedEventPublisher;
import com.nvlhnn.product.service.domain.ports.output.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class ProductHelper {

    private final ProductDomainService productDomainService;
    private final ProductRepository productRepository;
    private final ProductDataMapper productDataMapper;

    public ProductHelper(ProductDomainService productDomainService,
                         ProductRepository productRepository,
                         ProductDataMapper productDataMapper,
                         ProductCreatedEventPublisher productCreatedEventPublisher) {
        this.productDomainService = productDomainService;
        this.productRepository = productRepository;
        this.productDataMapper = productDataMapper;
    }

    @Transactional
    public ProductCreatedEvent persistProduct(CreateProductCommand productCommand) {
        Product product = productDataMapper.productCommandToProduct(productCommand);

        ProductCreatedEvent productCreatedEvent = productDomainService.initializeProduct(product, null );
        saveProduct(product);
        log.info("Product with name: {} has been successfully added", product.getName());
        return productCreatedEvent;
    }

    @Transactional
    public Product fetchProduct(ProductId productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductDomainException("Product not found."));

        log.info("Product with ID: {} has been successfully fetched", productId);
        return product;
    }

    private void saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        if (savedProduct == null) {
            log.error("Could not save product!");
            throw new ProductDomainException("Could not save product!");
        }
        log.info("Product is saved with name: {}", savedProduct.getName());
    }
}
