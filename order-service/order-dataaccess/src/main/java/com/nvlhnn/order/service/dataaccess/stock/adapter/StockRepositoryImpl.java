package com.nvlhnn.order.service.dataaccess.stock.adapter;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.order.service.dataaccess.stock.mapper.StockDataAccessMapper;
import com.nvlhnn.order.service.dataaccess.stock.repository.StockJpaRepository;
import com.nvlhnn.order.service.domain.entity.Stock;
import com.nvlhnn.order.service.domain.ports.output.repository.StockRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StockRepositoryImpl implements StockRepository {

    private final StockJpaRepository stockJpaRepository;
    private final StockDataAccessMapper stockDataAccessMapper;

    public StockRepositoryImpl(StockJpaRepository stockJpaRepository, StockDataAccessMapper stockDataAccessMapper) {
        this.stockJpaRepository = stockJpaRepository;
        this.stockDataAccessMapper = stockDataAccessMapper;
    }

    @Override
    public Stock save(Stock stock) {
        return stockDataAccessMapper.stockEntityToStock(stockJpaRepository
                .save(stockDataAccessMapper.stockToStockEntityDataMapper(stock)));
    }


    @Override
    public Optional<Stock> findByWarehouseIdAndProductId(WarehouseId warehouseId, ProductId productId) {
        return stockJpaRepository.findByWarehouseIdAndProductId(warehouseId.getValue(), productId.getValue())
                .map(stockDataAccessMapper::stockEntityToStock);
    }

    @Override
    public List<Stock> findByWarehouseId(WarehouseId warehouseId) {
        return stockJpaRepository.findByWarehouseId(warehouseId.getValue()).stream()
                .map(stockDataAccessMapper::stockEntityToStock)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Stock stock) {
        stockJpaRepository.delete(stockDataAccessMapper.stockToStockEntityDataMapper(stock));
    }

    @Override
    public Optional<List<Stock>> findByProductIdIn(List<ProductId> productIds) {
        return stockJpaRepository.findByProductIdIn(productIds.stream().map(ProductId::getValue).collect(Collectors.toList()))
                .map(stocks -> stocks.stream().map(stockDataAccessMapper::stockEntityToStock).collect(Collectors.toList()));
    }
}
