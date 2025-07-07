package com.acme.ezpark.platform.shared.infrastructure.web;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class GoogleCloudStorageService {

    @Autowired(required = false)
    private Storage storage;

    @Value("${google.cloud.storage.bucket}")
    private String bucketName;

    @Value("${google.cloud.storage.enabled:false}")
    private boolean gcsEnabled;

    public String uploadFile(MultipartFile file) throws IOException {
        if (!gcsEnabled || storage == null) {
            throw new RuntimeException("Google Cloud Storage is not enabled or configured");
        }

        String fileName = generateFileName(file);
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(file.getContentType())
                .build();

        Blob blob = storage.create(blobInfo, file.getBytes());
        
        // Return the public URL
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }

    public void deleteFile(String fileName) {
        if (!gcsEnabled || storage == null) {
            return;
        }

        storage.delete(bucketName, fileName);
    }

    private String generateFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        return UUID.randomUUID().toString() + extension;
    }

    public boolean isEnabled() {
        return gcsEnabled && storage != null;
    }
}
