// package com.nvlhnn.product.service.domain.ports.output.service;

package com.nvlhnn.product.service.domain.ports.output.service;

import com.nvlhnn.product.service.domain.exception.ImageUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
    /**
     * Uploads an image and returns its secure URL.
     *
     * @param file The image file to upload.
     * @return The URL of the uploaded image.
     * @throws ImageUploadException If the upload fails.
     */
    String uploadImage(MultipartFile file) throws ImageUploadException;
}
