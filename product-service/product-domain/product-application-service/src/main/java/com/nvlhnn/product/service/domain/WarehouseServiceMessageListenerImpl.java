package com.nvlhnn.product.service.domain;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.product.service.domain.dto.message.StockResponseMessage;
import com.nvlhnn.product.service.domain.dto.message.UserResponseMessage;
import com.nvlhnn.product.service.domain.entity.Product;
import com.nvlhnn.product.service.domain.entity.User;
import com.nvlhnn.product.service.domain.exception.ProductDomainException;
import com.nvlhnn.product.service.domain.mapper.ProductDataMapper;
import com.nvlhnn.product.service.domain.ports.input.message.listener.warehouse.WarehouseServiceMessageListener;
import com.nvlhnn.product.service.domain.ports.output.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class WarehouseServiceMessageListenerImpl implements WarehouseServiceMessageListener {

    private final ProductRepository productRepository;
    private final ProductDataMapper productDataMapper;
    private final ProductDomainService productDomainService;

    public WarehouseServiceMessageListenerImpl(ProductRepository productRepository,
                                               ProductDomainService productDomainService,
                                               ProductDataMapper productDataMapper) {
        this.productRepository = productRepository;
        this.productDataMapper = productDataMapper;
        this.productDomainService = productDomainService;
    }

    @Override
    public void onStockSaved(StockResponseMessage stockResponseMessage) {

        Product product = productRepository.findById(new ProductId(UUID.fromString(stockResponseMessage.getProductId()))).orElseThrow(() -> new ProductDomainException("Product not found."));
        productDomainService.updateProduct(product, stockResponseMessage.getTotalStockQuantity());

        Product savedProduct = productRepository.save(product);
        if (savedProduct == null) {
            log.error("Could not save product!");
            throw new ProductDomainException("Could not save product!");
        }
        log.info("Product is saved with name: {} and total stock quantity: {}", savedProduct.getName(), savedProduct.getTotalStock());
    }

}
