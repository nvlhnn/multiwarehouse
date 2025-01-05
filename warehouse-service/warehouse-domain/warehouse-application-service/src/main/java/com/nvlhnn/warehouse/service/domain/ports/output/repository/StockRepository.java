package com.nvlhnn.warehouse.service.domain.ports.output.repository;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.valueobject.StockTransfer;

import java.util.List;
import java.util.Optional;

public interface StockRepository {

    Stock save(Stock stock);

    Optional<Stock> findByWarehouseIdAndProductId(WarehouseId warehouseId, ProductId productId);

    List<Stock> findByWarehouseId(WarehouseId warehouseId);

    void delete(Stock stock);

    StockTransfer saveStockTransfer(StockTransfer stockTransfer);

    Optional<List<Stock>> findByProductIdIn(List<ProductId> productIds);

    Integer getProductTotalQuantity(ProductId productId);


}
