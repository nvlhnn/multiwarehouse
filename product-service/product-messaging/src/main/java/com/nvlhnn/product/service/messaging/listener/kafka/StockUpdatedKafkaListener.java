package com.nvlhnn.product.service.messaging.listener.kafka;

import com.nvlhnn.kafka.consumer.KafkaConsumer;
import com.nvlhnn.product.service.domain.dto.message.StockResponseMessage;
import com.nvlhnn.product.service.domain.ports.input.message.listener.warehouse.WarehouseServiceMessageListener;
import com.nvlhnn.product.service.messaging.mapper.ProductMessagingDataMapper;
import com.nvlhnn.warehouse.kafka.avro.model.StockUpdatedAvroModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StockUpdatedKafkaListener
        implements KafkaConsumer<StockUpdatedAvroModel>
{

    private final WarehouseServiceMessageListener warehouseServiceMessageListener;
    private final ProductMessagingDataMapper productMessagingDataMapper;

    public StockUpdatedKafkaListener(
            WarehouseServiceMessageListener warehouseServiceMessageListener,
                                      ProductMessagingDataMapper productMessagingDataMapper
    ) {
        this.warehouseServiceMessageListener = warehouseServiceMessageListener;
        this.productMessagingDataMapper = productMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.product-service-warehouse-stock-updated-group-id}",
            topics = "${product-service.stock-updated-topic-name}")
    public void receive(@Payload List<StockUpdatedAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {


        log.info("{} number of stock responses received with keys: {}, partitions: {}, and offsets: {}", messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        messages.forEach(stockUpdatedAvroModel -> {
            log.info("Processing product update with id: {} and total quantity: {}", stockUpdatedAvroModel.getProductId(), stockUpdatedAvroModel.getTotalProductStock());
            StockResponseMessage stockResponseMessage = productMessagingDataMapper
                    .stockUpdatedAvroModelToStockResponseMessage(stockUpdatedAvroModel);

            warehouseServiceMessageListener.onStockSaved(stockResponseMessage);
        });
    }
}
