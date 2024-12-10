package com.nvlhnn.order.service.domain.ports.input.message.listener.warehouse;


import com.nvlhnn.order.service.domain.dto.message.StockCreatedResponseMessage;
import com.nvlhnn.order.service.domain.dto.message.StockUpdatedResponseMessage;
import com.nvlhnn.order.service.domain.dto.message.WarehouseResponse;

public interface WarehouseResponseMessageListener {

    void onWarehouseSave(WarehouseResponse warehouseResponse);

    void onStockCreated(StockCreatedResponseMessage stockCreatedResponseMessage);

    void onStockUpdated(StockUpdatedResponseMessage stockUpdatedResponseMessage);

}
