package com.acme.ezpark.platform.shared.interfaces.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/debug")
@Tag(name = "Debug", description = "Debug endpoints for troubleshooting")
public class DebugController {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Operation(summary = "Check images status", description = "Debug endpoint to check images and static resources")
    @GetMapping("/images-status")
    public ResponseEntity<Map<String, Object>> getImagesStatus() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            File uploadDirFile = new File(uploadDir);
            String absoluteUploadDir = uploadDirFile.getAbsolutePath();
            
            response.put("uploadDir", uploadDir);
            response.put("absoluteUploadDir", absoluteUploadDir);
            response.put("uploadDirExists", uploadDirFile.exists());
            response.put("uploadDirIsDirectory", uploadDirFile.isDirectory());
            response.put("uploadDirCanRead", uploadDirFile.canRead());
            
            // Check parking images directory
            File parkingImagesDir = new File(uploadDirFile, "parking-images");
            response.put("parkingImagesDirExists", parkingImagesDir.exists());
            response.put("parkingImagesDirIsDirectory", parkingImagesDir.isDirectory());
            response.put("parkingImagesDirCanRead", parkingImagesDir.canRead());
            
            if (parkingImagesDir.exists() && parkingImagesDir.isDirectory()) {
                File[] imageFiles = parkingImagesDir.listFiles();
                List<Map<String, Object>> filesList = new ArrayList<>();
                
                if (imageFiles != null) {
                    for (File file : imageFiles) {
                        Map<String, Object> fileInfo = new HashMap<>();
                        fileInfo.put("name", file.getName());
                        fileInfo.put("size", file.length());
                        fileInfo.put("lastModified", new Date(file.lastModified()));
                        fileInfo.put("canRead", file.canRead());
                        fileInfo.put("absolutePath", file.getAbsolutePath());
                        filesList.add(fileInfo);
                    }
                }
                
                response.put("parkingImagesCount", imageFiles != null ? imageFiles.length : 0);
                response.put("parkingImagesList", filesList);
            }
            
            // Check profile pictures directory
            File profilePicturesDir = new File(uploadDirFile, "profile-pictures");
            response.put("profilePicturesDirExists", profilePicturesDir.exists());
            
            if (profilePicturesDir.exists() && profilePicturesDir.isDirectory()) {
                File[] profileFiles = profilePicturesDir.listFiles();
                response.put("profilePicturesCount", profileFiles != null ? profileFiles.length : 0);
            }
            
            // System information
            response.put("javaVersion", System.getProperty("java.version"));
            response.put("osName", System.getProperty("os.name"));
            response.put("userDir", System.getProperty("user.dir"));
            response.put("tempDir", System.getProperty("java.io.tmpdir"));
            
            response.put("success", true);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("stackTrace", Arrays.toString(e.getStackTrace()));
        }
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Test specific image", description = "Test if a specific image file can be accessed")
    @GetMapping("/test-image/{filename}")
    public ResponseEntity<Map<String, Object>> testSpecificImage(@PathVariable String filename) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            File uploadDirFile = new File(uploadDir);
            File parkingImagesDir = new File(uploadDirFile, "parking-images");
            File imageFile = new File(parkingImagesDir, filename);
            
            response.put("filename", filename);
            response.put("requestedPath", imageFile.getAbsolutePath());
            response.put("exists", imageFile.exists());
            response.put("isFile", imageFile.isFile());
            response.put("canRead", imageFile.canRead());
            response.put("size", imageFile.exists() ? imageFile.length() : 0);
            
            if (imageFile.exists()) {
                response.put("lastModified", new Date(imageFile.lastModified()));
                response.put("publicUrl", "/parking-images/" + filename);
            }
            
            response.put("success", true);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}
