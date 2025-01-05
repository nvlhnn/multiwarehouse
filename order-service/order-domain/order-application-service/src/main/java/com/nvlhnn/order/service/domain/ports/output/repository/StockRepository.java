package com.nvlhnn.order.service.domain.ports.output.repository;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.order.service.domain.entity.Stock;
import com.nvlhnn.order.service.domain.entity.Warehouse;

import java.util.List;
import java.util.Optional;

public interface StockRepository {
    Stock save(Stock stock);

    Optional<Stock> findByWarehouseIdAndProductId(WarehouseId warehouseId, ProductId productId);

    List<Stock> findByWarehouseId(WarehouseId warehouseId);

    void delete(Stock stock);


    Optional<List<Stock>> findByProductIdIn(List<ProductId> productIds);

}
