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
            System.out.println("Google Cloud Storage no está habilitado, no se puede eliminar: " + fileName);
            return;
        }

        try {
            // Extract filename from full URL if necessary
            String actualFileName = extractFileNameFromUrl(fileName);
            System.out.println("Eliminando archivo de GCS: " + actualFileName);
            
            boolean deleted = storage.delete(bucketName, actualFileName);
            if (deleted) {
                System.out.println("✅ Archivo eliminado exitosamente de GCS: " + actualFileName);
            } else {
                System.out.println("⚠️ El archivo no existía en GCS: " + actualFileName);
            }
        } catch (Exception e) {
            System.err.println("❌ Error eliminando archivo de GCS: " + e.getMessage());
        }
    }

    private String extractFileNameFromUrl(String fileUrl) {
        // Si es una URL completa de GCS, extraer solo el nombre del archivo
        if (fileUrl.contains("storage.googleapis.com")) {
            String[] parts = fileUrl.split("/");
            return parts[parts.length - 1];
        }
        
        // Si es solo un path local, extraer el nombre del archivo
        if (fileUrl.startsWith("/")) {
            String[] parts = fileUrl.split("/");
            return parts[parts.length - 1];
        }
        
        // Si ya es solo el nombre del archivo, retornarlo
        return fileUrl;
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
