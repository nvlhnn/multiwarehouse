package com.nvlhnn.product.service.domain;

import com.nvlhnn.product.service.domain.dto.post.CreateProductCommand;
import com.nvlhnn.product.service.domain.dto.response.ProductResponse;
import com.nvlhnn.product.service.domain.event.ProductCreatedEvent;
import com.nvlhnn.product.service.domain.exception.ImageUploadException;
import com.nvlhnn.product.service.domain.exception.ProductDomainException;
import com.nvlhnn.product.service.domain.mapper.ProductDataMapper;
import com.nvlhnn.product.service.domain.ports.output.message.publisher.ProductCreatedEventPublisher;
import com.nvlhnn.product.service.domain.ports.output.service.ImageUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.math.BigDecimal;

@Slf4j
@Component
public class ProductCreateCommandHandler {

    private final ProductHelper productHelper;
    private final ProductDataMapper productDataMapper;
    private final ProductCreatedEventPublisher productCreatedEventPublisher;
    private final ImageUploadService imageUploadService;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    public ProductCreateCommandHandler(ProductHelper productHelper,
                                       ProductDataMapper productDataMapper,
                                       ImageUploadService imageUploadService,
                                       ProductCreatedEventPublisher productCreatedEventPublisher) {
        this.productHelper = productHelper;
        this.productDataMapper = productDataMapper;
        this.productCreatedEventPublisher = productCreatedEventPublisher;
        this.imageUploadService = imageUploadService;
    }

    public ProductResponse createProduct(String name, BigDecimal price, MultipartFile file) {

        // Validate image file
        if (file.isEmpty()) {
            throw new ProductDomainException("Image file is empty");
        }

        // Optional: Validate file type (could also be moved to a separate validator)
        String contentType = file.getContentType();
        if (!isImage(contentType)) {
            throw new ProductDomainException("Invalid image type. Only JPEG and PNG are allowed.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ProductDomainException("Image file size exceeds the maximum limit of 5MB.");
        }

        String imageUrl;
        try {
            imageUrl = imageUploadService.uploadImage(file);
        } catch (ImageUploadException e) {
            throw new ImageUploadException("Image upload failed: " + e.getMessage(), e);
        }

        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .name(name)
                .price(price)
                .imageUrl(imageUrl)
                .build();

        ProductCreatedEvent productCreatedEvent = productHelper.persistProduct(createProductCommand);
        log.info("Product with name: {} has been successfully created", productCreatedEvent.getProduct().getName());
        productCreatedEventPublisher.publish(productCreatedEvent);  // Publish product created event
        return productDataMapper.productToProductResponse(productCreatedEvent.getProduct(), "Product created successfully");
    }

    private boolean isImage(String contentType) {
        return contentType.equalsIgnoreCase("image/jpeg") ||
                contentType.equalsIgnoreCase("image/png") ||
                contentType.equalsIgnoreCase("image/jpg") ||
                contentType.equalsIgnoreCase("image/gif");
    }
}
