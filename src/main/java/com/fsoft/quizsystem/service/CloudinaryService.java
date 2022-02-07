package com.fsoft.quizsystem.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(String oldUrl, MultipartFile file) {
        try {
            if (oldUrl != null) {
                String publicId = extractPublicIdFromUrl(oldUrl);
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return result.get("url").toString();
        } catch (Exception ex) {
            log.error("Failed to load to Cloudinary the image file: " + file.getName());
            log.error(ex);
            return null;
        }
    }

    private String extractPublicIdFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
    }
}
