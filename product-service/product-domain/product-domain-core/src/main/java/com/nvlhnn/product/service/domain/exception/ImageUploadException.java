// package com.nvlhnn.product.service.domain.exception;

package com.nvlhnn.product.service.domain.exception;

public class ImageUploadException extends RuntimeException {
    public ImageUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageUploadException(String message) {
        super(message);
    }
}
