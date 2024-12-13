package com.nvlhnn.order.service.domain;

import com.nvlhnn.order.service.domain.dto.message.StockCreatedResponseMessage;
import com.nvlhnn.order.service.domain.dto.message.StockUpdatedResponseMessage;
import com.nvlhnn.order.service.domain.dto.message.WarehouseResponseMessage;
import com.nvlhnn.order.service.domain.entity.Stock;
import com.nvlhnn.order.service.domain.exception.OrderDomainException;
import com.nvlhnn.order.service.domain.mapper.OrderDataMapper;
import com.nvlhnn.order.service.domain.ports.input.message.listener.warehouse.WarehouseResponseMessageListener;
import com.nvlhnn.order.service.domain.ports.output.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WarehouseResponseMessageListenerImpl implements WarehouseResponseMessageListener {

    private final WarehouseSaveSaga warehouseSaveSaga;
    private final OrderDataMapper orderDataMapper;
    private final StockRepository stockRepository;

    public WarehouseResponseMessageListenerImpl(WarehouseSaveSaga warehouseSaveSaga,
                                                OrderDataMapper orderDataMapper,
                                                StockRepository stockRepository

    ) {
        this.orderDataMapper = orderDataMapper;
        this.warehouseSaveSaga = warehouseSaveSaga;
        this.stockRepository = stockRepository;
    }

    @Override
    public void onWarehouseSave(WarehouseResponseMessage warehouseResponseMessage) {

        log.info("Received WarehouseSave event for warehouse id: {}", warehouseResponseMessage.getWarehoudId());
        warehouseSaveSaga.process(warehouseResponseMessage);

    }

    @Override
    public void onStockCreated(StockCreatedResponseMessage stockCreatedResponseMessage) {

        log.info("Received StockCreated event for stock id: {}", stockCreatedResponseMessage.getStockId());

        Stock stock = stockRepository.save(orderDataMapper.stockCreatedResponseMessageToStock(stockCreatedResponseMessage));
        if (stock == null) {
            log.error("Stock is not saved with id: {}", stock.getId().getValue().toString());
            throw new OrderDomainException("Stock is not saved with id: " + stock.getId().getValue().toString());
        }

        log.info("Stock is saved with id: {}", stockCreatedResponseMessage.getStockId().toString());

    }

    @Override
    public void onStockUpdated(StockUpdatedResponseMessage stockUpdatedMessage) {

        log.info("Received StockUpdated event for stock id: {}", stockUpdatedMessage.getStockId());

        Stock stock = stockRepository.save(orderDataMapper.stockUpdatedResponseMessageToStock(stockUpdatedMessage));
        if (stock == null) {
            log.error("Stock is not saved with id: {}", stock.getId().getValue().toString());
            throw new OrderDomainException("Stock is not saved with id: " + stock.getId().getValue().toString());
        }
        log.info("Stock is saved with id: {}", stockUpdatedMessage.getStockId().toString());

    }
    

}
