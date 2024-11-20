package com.nvlhnn.warehouse.service.domain.exception;

import com.nvlhnn.domain.exception.DomainException;

public class WarehouseDomainException extends com.nvlhnn.domain.exception.DomainException {

    public WarehouseDomainException(String message) {
        super(message);
    }

    public WarehouseDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
