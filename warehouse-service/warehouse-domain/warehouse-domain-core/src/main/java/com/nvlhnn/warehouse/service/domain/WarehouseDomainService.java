package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.warehouse.service.domain.entity.Product;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.entity.User;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;
import com.nvlhnn.warehouse.service.domain.event.*;
import com.nvlhnn.warehouse.service.domain.valueobject.StreetAddress;

import java.util.Optional;

public interface WarehouseDomainService {

    WarehouseCreatedEvent createWarehouse(Warehouse warehouse, DomainEventPublisher<WarehouseCreatedEvent> publisher);
    WarehouseUpdatedEvent updateWarehouse(Warehouse warehouse, String newName, StreetAddress newStreetAddress, DomainEventPublisher<WarehouseUpdatedEvent> publisher);

    //    void deleteWarehouse(Warehouse warehouse);
    //    void assignWarehouseAdmin(Warehouse warehouse, User user);

    StockCreatedEvent createStock(Stock stock, Warehouse warehouse, Product product, DomainEventPublisher<StockCreatedEvent> publisher);
    StockUpdatedEvent updateStock(Stock stock, int quantity, DomainEventPublisher<StockUpdatedEvent> publisher);

    StockTransferredEvent transferStock(Warehouse fromWarehouse, Warehouse toWarehouse, Stock fromStock, Optional<Stock> toStock, Product product, int quantity, DomainEventPublisher<StockTransferredEvent> publisher);


    void createUser(User user);

}
