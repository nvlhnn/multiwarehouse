package com.nvlhnn.order.service.domain.ports.input.message.listener.warehouse;


import com.nvlhnn.order.service.domain.dto.message.WarehouseResponse;

public interface WarehouseResponseMessageListener {

    void onWarehouseSave(WarehouseResponse warehouseResponse);

}
