package com.nvlhnn.product.service.domain.ports.input.service;

import com.nvlhnn.product.service.domain.dto.post.CreateProductCommand;
import com.nvlhnn.product.service.domain.dto.response.ProductResponse;

import javax.validation.Valid;

public interface ProductApplicationService {

    ProductResponse createProduct(@Valid CreateProductCommand createProductCommand);

}
