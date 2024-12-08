package com.nvlhnn.product.service.domain.rest;

import com.nvlhnn.product.service.domain.dto.post.CreateProductCommand;
import com.nvlhnn.product.service.domain.dto.response.ProductResponse;
import com.nvlhnn.product.service.domain.ports.input.service.ProductApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/products", produces = "application/vnd.api.v1+json")
public class ProductController {

    private final ProductApplicationService productApplicationService;

    public ProductController(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    @PostMapping()
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid CreateProductCommand createProductCommand) {
        log.info("Creating product with name: {}", createProductCommand.getName());
        ProductResponse productResponse = productApplicationService.createProduct(createProductCommand);
        log.info("Product created with id {}", productResponse.getProductId());
        return ResponseEntity.ok(productResponse);
    }


}
