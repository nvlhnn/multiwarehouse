package com.nvlhnn.user.service.domain.exception;

import com.nvlhnn.domain.exception.DomainException;

public class UserDomainException extends DomainException {

  public UserDomainException(String message) {
    super(message);
  }

  public UserDomainException(String message, Throwable cause) {
    super(message, cause);
  }
}
