package com.nvlhnn.warehouse.service.domain.ports.output.repository;

import com.nvlhnn.warehouse.service.domain.valueobject.StockTransfer;

public interface StockTransferRepository {
    StockTransfer save(StockTransfer stockTransfer);

}
