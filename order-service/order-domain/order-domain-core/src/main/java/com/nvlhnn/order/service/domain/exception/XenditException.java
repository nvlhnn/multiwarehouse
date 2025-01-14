package com.nvlhnn.order.service.domain.exception;

public class XenditException extends RuntimeException {
    public XenditException(String message, Throwable cause) {
        super(message, cause);
    }

    public XenditException(String message) {
        super(message);
    }
}
