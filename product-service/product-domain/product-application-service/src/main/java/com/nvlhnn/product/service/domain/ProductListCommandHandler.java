package com.nvlhnn.product.service.domain;

import com.nvlhnn.product.service.domain.dto.response.ProductListResponse;
import com.nvlhnn.product.service.domain.dto.response.ProductResponse;
import com.nvlhnn.product.service.domain.entity.Product;
import com.nvlhnn.product.service.domain.mapper.ProductDataMapper;
import com.nvlhnn.product.service.domain.ports.output.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductListCommandHandler {

    private final ProductRepository productRepository;
    private final ProductDataMapper productDataMapper;

    public ProductListCommandHandler(ProductRepository productRepository,
                                     ProductDataMapper productDataMapper) {
        this.productRepository = productRepository;
        this.productDataMapper = productDataMapper;
    }

    public ProductListResponse listProducts(int page, int size) {
        log.info("Listing products with page: {} and size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<Product> productPage = productRepository.findAll(pageable);

        return productDataMapper.productPageToProductListResponse(productPage);
    }
}
