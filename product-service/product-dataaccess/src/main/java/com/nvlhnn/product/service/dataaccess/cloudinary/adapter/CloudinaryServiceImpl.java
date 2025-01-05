package com.nvlhnn.product.service.dataaccess.cloudinary.adapter;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvlhnn.product.service.domain.exception.ImageUploadException;
import com.nvlhnn.product.service.domain.ports.output.service.ImageUploadService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryServiceImpl  implements ImageUploadService {
    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile file) throws ImageUploadException {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "products"));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new ImageUploadException("Failed to upload image to Cloudinary", e);
        }
    }
}
