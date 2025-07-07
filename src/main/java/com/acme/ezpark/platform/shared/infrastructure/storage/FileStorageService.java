package com.acme.ezpark.platform.shared.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private final List<String> allowedImageTypes = List.of(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    private final long maxFileSize = 5 * 1024 * 1024; // 5MB

    public String storeFile(MultipartFile file, String subfolder) throws IOException {
        validateFile(file);
        
        // Create uploads directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir, subfolder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // Save file
        Path targetLocation = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), targetLocation);

        // Return relative URL
        return "/" + subfolder + "/" + uniqueFilename;
    }

    public List<String> storeMultipleFiles(List<MultipartFile> files, String subfolder, int maxFiles) throws IOException {
        if (files == null || files.isEmpty()) {
            return new ArrayList<>();
        }

        if (files.size() > maxFiles) {
            throw new IllegalArgumentException("Maximum " + maxFiles + " files allowed");
        }

        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileUrl = storeFile(file, subfolder);
                fileUrls.add(fileUrl);
            }
        }

        return fileUrls;
    }

    public void deleteFile(String fileUrl) {
        try {
            if (fileUrl != null && fileUrl.startsWith("/")) {
                Path filePath = Paths.get(uploadDir + fileUrl);
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            // Log error but don't throw exception
            System.err.println("Error deleting file: " + fileUrl);
        }
    }

    public void deleteMultipleFiles(List<String> fileUrls) {
        if (fileUrls != null) {
            fileUrls.forEach(this::deleteFile);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size (5MB)");
        }

        String contentType = file.getContentType();
        if (contentType == null || !allowedImageTypes.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Only image files are allowed (JPEG, PNG, GIF, WebP)");
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return ".jpg"; // Default extension
        }
        return filename.substring(filename.lastIndexOf('.'));
    }
}
