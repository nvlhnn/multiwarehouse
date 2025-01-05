package com.nvlhnn.product.service.messaging.listener.kafka;

import com.nvlhnn.kafka.consumer.KafkaConsumer;
import com.nvlhnn.product.service.domain.dto.message.StockResponseMessage;
import com.nvlhnn.product.service.domain.ports.input.message.listener.warehouse.WarehouseServiceMessageListener;
import com.nvlhnn.product.service.messaging.mapper.ProductMessagingDataMapper;
import com.nvlhnn.warehouse.kafka.avro.model.StockCreatedAvroModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StockCreatedKafkaListener
        implements KafkaConsumer<StockCreatedAvroModel>
{

    private final WarehouseServiceMessageListener warehouseServiceMessageListener;
    private final ProductMessagingDataMapper productMessagingDataMapper;

    public StockCreatedKafkaListener(
            WarehouseServiceMessageListener warehouseServiceMessageListener,
                                      ProductMessagingDataMapper productMessagingDataMapper
    ) {
        this.warehouseServiceMessageListener = warehouseServiceMessageListener;
        this.productMessagingDataMapper = productMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.product-service-warehouse-stock-created-group-id}",
            topics = "${product-service.stock-created-topic-name}")
    public void receive(@Payload List<StockCreatedAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {


        log.info("{} number of stock responses received with keys: {}, partitions: {}, and offsets: {}", messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        messages.forEach(stockCreatedAvroModel -> {
            log.info("Processing stock response with id: {}", stockCreatedAvroModel.getProductId());
            StockResponseMessage stockResponseMessage = productMessagingDataMapper
                    .stockCreatedAvroModelToStockResponseMessage(stockCreatedAvroModel);
            warehouseServiceMessageListener.onStockSaved(stockResponseMessage);
        });
    }
}
