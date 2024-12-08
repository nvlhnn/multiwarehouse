package com.nvlhnn.warehouse.service.domain.ports.input.message.listener.product;

import com.nvlhnn.warehouse.service.domain.dto.message.ProductResponseMessage;
import com.nvlhnn.warehouse.service.domain.dto.message.UserResponseMessage;

public interface ProductServiceMessageListener {

    void onProductCreated(ProductResponseMessage productResponseMessage);
}
