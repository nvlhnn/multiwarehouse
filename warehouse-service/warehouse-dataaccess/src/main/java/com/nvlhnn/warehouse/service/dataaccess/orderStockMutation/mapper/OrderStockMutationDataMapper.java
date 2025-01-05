package com.nvlhnn.warehouse.service.dataaccess.orderStockMutation.mapper;

import com.nvlhnn.domain.valueobject.OrderId;
import com.nvlhnn.domain.valueobject.OrderStockMutationId;
import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.dataaccess.orderStockMutation.entity.OrderStockMutationEntity;
import com.nvlhnn.warehouse.service.dataaccess.product.entity.ProductEntity;
import com.nvlhnn.warehouse.service.domain.entity.OrderStockMutation;
import com.nvlhnn.warehouse.service.domain.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class OrderStockMutationDataMapper {

    public OrderStockMutation orderStockMutationEntityToOrderStockMutationDataMapper(OrderStockMutationEntity orderStockMutationEntity) {
       return OrderStockMutation.builder()
               .orderStockMutationId(new OrderStockMutationId(orderStockMutationEntity.getId()))
               .orderId(new OrderId(orderStockMutationEntity.getOrderId()))
               .warehouseId(new WarehouseId(orderStockMutationEntity.getWarehouseId()))
               .productId(new ProductId(orderStockMutationEntity.getProductId()))
               .quantity(orderStockMutationEntity.getQuantity())
               .statusStockMutation(orderStockMutationEntity.getStatusStockMutation())
               .build();
    }

    public OrderStockMutationEntity orderStockMutationToOrderStockMutationEntityDataMapper(OrderStockMutation orderStockMutation) {
        return OrderStockMutationEntity.builder()
                .id(orderStockMutation.getId().getValue())
                .orderId(orderStockMutation.getOrderId().getValue())
                .warehouseId(orderStockMutation.getWarehouseId().getValue())
                .productId(orderStockMutation.getProductId().getValue())
                .quantity(orderStockMutation.getQuantity())
                .statusStockMutation(orderStockMutation.getStatusStockMutation())
                .build();
    }
}
