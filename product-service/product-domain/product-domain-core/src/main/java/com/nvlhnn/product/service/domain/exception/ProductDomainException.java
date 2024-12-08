package com.nvlhnn.product.service.domain.exception;

import com.nvlhnn.domain.exception.DomainException;

public class ProductDomainException extends DomainException {

  public ProductDomainException(String message) {
    super(message);
  }

  public ProductDomainException(String message, Throwable cause) {
    super(message, cause);
  }
}
