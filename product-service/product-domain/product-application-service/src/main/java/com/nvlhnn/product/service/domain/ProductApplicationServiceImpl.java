package com.nvlhnn.product.service.domain;

import com.nvlhnn.product.service.domain.dto.post.CreateProductCommand;
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

    public ProductApplicationServiceImpl(ProductCreateCommandHandler createProductCommandHandler) {
        this.createProductCommandHandler = createProductCommandHandler;
    }

    @Override
    public ProductResponse createProduct(CreateProductCommand createProductCommand) {
        log.info("Creating product with name: {}", createProductCommand.getName());
        return createProductCommandHandler.createProduct(createProductCommand);
    }
}
