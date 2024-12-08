package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.warehouse.service.domain.dto.message.ProductResponseMessage;
import com.nvlhnn.warehouse.service.domain.dto.message.UserResponseMessage;
import com.nvlhnn.warehouse.service.domain.entity.Product;
import com.nvlhnn.warehouse.service.domain.entity.User;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseDomainException;
import com.nvlhnn.warehouse.service.domain.mapper.WarehouseDataMapper;
import com.nvlhnn.warehouse.service.domain.ports.input.message.listener.product.ProductServiceMessageListener;
import com.nvlhnn.warehouse.service.domain.ports.input.message.listener.user.UserServiceMessageListener;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.ProductRepository;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductServiceMessageListenerImpl implements ProductServiceMessageListener {

    private final ProductRepository productRepository;
    private final WarehouseDataMapper warehouseDataMapper;

    public ProductServiceMessageListenerImpl(ProductRepository productRepository, WarehouseDataMapper warehouseDataMapper) {
        this.productRepository = productRepository;
        this.warehouseDataMapper = warehouseDataMapper;
    }

    @Override
    public void onProductCreated(ProductResponseMessage productResponseMessage) {

        Product mappedProduct = warehouseDataMapper.productResponseMessageToProduct(productResponseMessage);

        log.info("product price: {}", mappedProduct.getPrice());
        log.info("product price message: {}", productResponseMessage.getPrice());

        Product product = productRepository.save(mappedProduct);
        if (product == null) {
            log.error("Product is not saved with name: {}", product.getName());
            throw new WarehouseDomainException("Product is not saved with name: " + product.getName());
        }

        log.info("Product is saved with id: {}", product.getId());
    }

}
