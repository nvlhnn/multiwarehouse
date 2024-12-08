package com.nvlhnn.product.service.domain;

import com.nvlhnn.product.service.domain.dto.post.CreateProductCommand;
import com.nvlhnn.product.service.domain.dto.response.ProductResponse;
import com.nvlhnn.product.service.domain.event.ProductCreatedEvent;
import com.nvlhnn.product.service.domain.mapper.ProductDataMapper;
import com.nvlhnn.product.service.domain.ports.output.message.publisher.ProductCreatedEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductCreateCommandHandler {

    private final ProductHelper productHelper;
    private final ProductDataMapper productDataMapper;
    private final ProductCreatedEventPublisher productCreatedEventPublisher;

    public ProductCreateCommandHandler(ProductHelper productHelper,
                                       ProductDataMapper productDataMapper,
                                       ProductCreatedEventPublisher productCreatedEventPublisher) {
        this.productHelper = productHelper;
        this.productDataMapper = productDataMapper;
        this.productCreatedEventPublisher = productCreatedEventPublisher;
    }

    public ProductResponse createProduct(CreateProductCommand createProductCommand) {
        ProductCreatedEvent productCreatedEvent = productHelper.persistProduct(createProductCommand);
        log.info("Product with name: {} has been successfully created", productCreatedEvent.getProduct().getName());
        productCreatedEventPublisher.publish(productCreatedEvent);  // Publish product created event
        return productDataMapper.productToProductResponse(productCreatedEvent.getProduct(), "Product created successfully");
    }
}
