package com.nvlhnn.warehouse.service.domain.ports.output.repository;

import com.nvlhnn.domain.valueobject.OrderId;
import com.nvlhnn.domain.valueobject.OrderStockMutationId;
import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.entity.OrderStockMutation;
import com.nvlhnn.warehouse.service.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface OrderStockMutationRepository {

    Optional<OrderStockMutation> findByWarehouseIdAndProductId(WarehouseId warehouseId, ProductId productId);

    OrderStockMutation save(OrderStockMutation orderStockMutation);

    Optional<List<OrderStockMutation>> findByOrderId(OrderId orderId);

    void updateStatusStockMutation(OrderStockMutation orderStockMutation);
}
