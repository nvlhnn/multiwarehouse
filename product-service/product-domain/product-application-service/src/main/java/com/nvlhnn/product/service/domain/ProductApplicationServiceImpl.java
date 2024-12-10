package com.nvlhnn.product.service.domain;

import com.nvlhnn.product.service.domain.dto.post.CreateProductCommand;
import com.nvlhnn.product.service.domain.dto.response.ProductListResponse;
import com.nvlhnn.product.service.domain.dto.response.ProductResponse;
import com.nvlhnn.product.service.domain.ports.input.service.ProductApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class ProductApplicationServiceImpl implements ProductApplicationService {

    private final ProductCreateCommandHandler createProductCommandHandler;
    private final ProductListCommandHandler productListCommandHandler;

    public ProductApplicationServiceImpl(ProductCreateCommandHandler createProductCommandHandler,
                                         ProductListCommandHandler productListCommandHandler) {
        this.createProductCommandHandler = createProductCommandHandler;
        this.productListCommandHandler = productListCommandHandler;
    }

    @Override
    public ProductResponse createProduct(CreateProductCommand createProductCommand) {
        log.info("Creating product with name: {}", createProductCommand.getName());
        return createProductCommandHandler.createProduct(createProductCommand);
    }

    @Override
    public ProductListResponse listProducts(int page, int size) {
        log.info("Listing products with page: {} and size: {}", page, size);
        return productListCommandHandler.listProducts(page, size);
    }
}
