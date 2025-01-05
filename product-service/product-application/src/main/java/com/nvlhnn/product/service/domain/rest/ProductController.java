package com.nvlhnn.product.service.domain.rest;

import com.nvlhnn.product.service.domain.dto.post.CreateProductCommand;
import com.nvlhnn.product.service.domain.dto.response.ProductListResponse;
import com.nvlhnn.product.service.domain.dto.response.ProductResponse;
import com.nvlhnn.product.service.domain.ports.input.service.ProductApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping(value = "/products", produces = "application/vnd.api.v1+json")
public class ProductController {

    private final ProductApplicationService productApplicationService;

    public ProductController(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    @PostMapping()
    public ResponseEntity<ProductResponse> createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") BigDecimal price,
            @RequestPart("image") MultipartFile file)  {
        log.info("Creating product with name: {}", name);
        return productApplicationService.createProduct(name, price, file);
    }

    // list products
    @GetMapping()
    public ResponseEntity<ProductListResponse> listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ProductListResponse response = productApplicationService.listProducts(page, size);
        return ResponseEntity.ok(response);
    }
}
