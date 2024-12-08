package com.nvlhnn.order.service.domain.ports.input.message.listener.product;

import com.nvlhnn.order.service.domain.dto.message.ProductResponseMessage;

public interface ProductServiceMessageListener {

    void onProductCreated(ProductResponseMessage productResponseMessage);
}
