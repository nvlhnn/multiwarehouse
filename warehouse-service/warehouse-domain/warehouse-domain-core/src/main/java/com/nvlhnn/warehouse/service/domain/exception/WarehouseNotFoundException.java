package com.nvlhnn.warehouse.service.domain.exception;

import com.nvlhnn.domain.exception.DomainException;

public class WarehouseNotFoundException extends com.nvlhnn.domain.exception.DomainException {
    public WarehouseNotFoundException(String message) {
        super(message);
    }
    public WarehouseNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
