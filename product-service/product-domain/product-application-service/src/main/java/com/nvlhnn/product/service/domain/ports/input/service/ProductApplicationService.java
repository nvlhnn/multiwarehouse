package com.nvlhnn.product.service.domain.ports.input.service;

import com.nvlhnn.product.service.domain.dto.post.CreateProductCommand;
import com.nvlhnn.product.service.domain.dto.response.ProductListResponse;
import com.nvlhnn.product.service.domain.dto.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.math.BigDecimal;

public interface ProductApplicationService {

    ResponseEntity<ProductResponse> createProduct(String name, BigDecimal price, MultipartFile file);

    ProductListResponse listProducts( int page, int size);

}
