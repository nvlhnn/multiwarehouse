package com.nvlhnn.warehouse.service.domain.mapper;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.UserId;
import com.nvlhnn.domain.valueobject.UserRole;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.dto.create.*;
import com.nvlhnn.warehouse.service.domain.dto.message.UserResponseMessage;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;
import com.nvlhnn.warehouse.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WarehouseDataMapper {

    public Warehouse createWarehouseCommandToWarehouse(CreateWarehouseCommand createWarehouseCommand){
        return Warehouse.builder()
                .name(createWarehouseCommand.getName())
                .streetAddress(warehouseAddressToStreetAddress(createWarehouseCommand.getWarehouseAddress()))
                .build();
    }

//    public Stock createSockCommandToStock(CreateStockCommand createStockCommand){
//        return Stock.builder()
//                .warehouseId(createStockCommand.getWarehouseId())
//                .productId(createStockCommand.getProductId())
//                .quantity(createStockCommand.getQuantity())
//                .build();
//    }

    public StreetAddress warehouseAddressToStreetAddress(WarehouseAddress warehouseAddress){
        return new StreetAddress(
                UUID.randomUUID(),
                warehouseAddress.getStreet(),
                warehouseAddress.getPostalCode(),
                warehouseAddress.getCity(),
                warehouseAddress.getLatitude(),
                warehouseAddress.getLongitude()
        );
    }

    public Stock createStockfromCreateStockCommand(CreateStockCommand createStockCommand){
        return Stock.builder()
                .warehouseId(new WarehouseId(createStockCommand.getWarehouseId()))
                .productId(new ProductId(createStockCommand.getProductId()))
                .quantity(createStockCommand.getQuantity())
                .build();

    }

    public CreateWarehouseResponse warehouseToCreateWarehouseResponse(Warehouse warehouse, String message){
        return CreateWarehouseResponse.builder()
                .warehouseId(warehouse.getId().getValue())
                .isActive(warehouse.isActive())
                .message(message)
                .build();
    }


    public CreateStockResponse stockToCreateStockResponse(Stock stock, String message){
        return CreateStockResponse.builder()
                .stockId(stock.getId().getValue())
                .warehouseId(stock.getWarehouseId().getValue())
                .productId(stock.getProductId().getValue())
                .message(message)
                .build();
    }
    public CreateTransferStockResponse buildStockTransferredResponse(int fromStock, int toStock, UUID productId, String message){
        return CreateTransferStockResponse.builder()
                .fromWarehouseQuantity(fromStock)
                .toWarehouseQuantity(toStock)
                .productId(productId)
                .message(message)
                .build();
    }

    public com.nvlhnn.warehouse.service.domain.entity.User userResponseMessageToUser(UserResponseMessage userResponseMessage) {
        return com.nvlhnn.warehouse.service.domain.entity.User.builder()
                .userId(new UserId(UUID.fromString(userResponseMessage.getUserId())))
                .email(userResponseMessage.getEmail())
                .name(userResponseMessage.getName())
                .role(UserRole.valueOf(userResponseMessage.getRole()))
                .token(userResponseMessage.getToken())
                .isActive(userResponseMessage.isActive())
                .build();
    }

}
