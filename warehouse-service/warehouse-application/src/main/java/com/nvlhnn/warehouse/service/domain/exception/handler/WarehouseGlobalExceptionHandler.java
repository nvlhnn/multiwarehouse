package com.nvlhnn.warehouse.service.domain.exception.handler;

import com.nvlhnn.application.handler.ErrorDTO;
import com.nvlhnn.application.handler.GlobalExceptionHandler;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseDomainException;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class WarehouseGlobalExceptionHandler extends GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = {WarehouseDomainException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(WarehouseDomainException warehouseDomainException) {
        log.error(warehouseDomainException.getMessage(), warehouseDomainException);
        return ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(warehouseDomainException.getMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {WarehouseNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleException(WarehouseNotFoundException warehouseNotfoundException) {
        log.error(warehouseNotfoundException.getMessage(), warehouseNotfoundException);
        return ErrorDTO.builder()
                .code(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(warehouseNotfoundException.getMessage())
                .build();
    }
}