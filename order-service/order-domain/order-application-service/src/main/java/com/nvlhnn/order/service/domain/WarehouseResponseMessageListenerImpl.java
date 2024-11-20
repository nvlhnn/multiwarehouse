package com.nvlhnn.order.service.domain;

import com.nvlhnn.order.service.domain.dto.message.WarehouseResponse;
import com.nvlhnn.order.service.domain.ports.input.message.listener.warehouse.WarehouseResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WarehouseResponseMessageListenerImpl implements WarehouseResponseMessageListener {

    private final WarehouseSaveSaga warehouseSaveSaga;

    public WarehouseResponseMessageListenerImpl(WarehouseSaveSaga warehouseSaveSaga) {
        this.warehouseSaveSaga = warehouseSaveSaga;
    }

    @Override
    public void onWarehouseSave(WarehouseResponse warehouseResponse) {

        log.info("Received WarehouseSave event for warehouse id: {}", warehouseResponse.getWarehoudId());
        warehouseSaveSaga.process(warehouseResponse);

    }

}
