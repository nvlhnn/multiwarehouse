package com.nvlhnn.order.service.domain;

import com.nvlhnn.order.service.domain.dto.message.WarehouseResponse;
import com.nvlhnn.order.service.domain.entity.Warehouse;
import com.nvlhnn.order.service.domain.mapper.OrderDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class WarehouseSaveSaga {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper sagaHelper;
    private final OrderDataMapper orderDataMapper;


    public WarehouseSaveSaga(OrderDomainService orderDomainService,
                             OrderSagaHelper sagaHelper,
                            OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.sagaHelper = sagaHelper;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public void process(WarehouseResponse warehouseResponse) {
        log.info("Processing save warehouse id: {}", warehouseResponse.getWarehoudId());

        Warehouse warehouse = orderDataMapper.warehouseResponseToWarehouse(warehouseResponse);
        orderDomainService.validateInitialWarehouse(warehouse);

        sagaHelper.saveWarehouse(warehouse);

        log.info("Save warehouse is completed with id: {}", warehouse.getId().getValue());
    }
}
