package com.acme.ezpark.platform.shared.interfaces.rest;

import com.acme.ezpark.platform.shared.infrastructure.storage.FileStorageService;
import com.acme.ezpark.platform.shared.infrastructure.web.GoogleCloudStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/uploads")
@Tag(name = "File Upload", description = "Endpoints for uploading images")
public class FileUploadController {

    private final FileStorageService fileStorageService;
    
    @Autowired(required = false)
    private GoogleCloudStorageService googleCloudStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Operation(summary = "Test Google Cloud Storage configuration", description = "Check if GCS is properly configured")
    @GetMapping("/test-gcs-config")
    public ResponseEntity<Map<String, Object>> testGcsConfig() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean gcsEnabled = googleCloudStorageService != null && googleCloudStorageService.isEnabled();
            response.put("gcsEnabled", gcsEnabled);
            response.put("gcsAvailable", googleCloudStorageService != null);
            response.put("storageType", gcsEnabled ? "Google Cloud Storage" : "Local Storage");
            response.put("message", gcsEnabled ? "GCS is properly configured" : "Using local storage fallback");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error checking GCS configuration: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Upload profile picture", description = "Upload a single profile picture for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile picture uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid file or file size exceeds limit"),
        @ApiResponse(responseCode = "500", description = "Error uploading file")
    })
    @PostMapping("/profile-picture")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(
            @Parameter(description = "Profile picture file", required = true)
            @RequestParam("file") MultipartFile file) {
        
        try {
            String imageUrl = fileStorageService.storeFile(file, "profile-pictures");
            
            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", imageUrl);
            response.put("message", "Profile picture uploaded successfully");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error uploading profile picture: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Upload parking images", description = "Upload multiple images for a parking space (max 3)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parking images uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid files, file size exceeds limit, or too many files"),
        @ApiResponse(responseCode = "500", description = "Error uploading files")
    })
    @PostMapping("/parking-images")
    public ResponseEntity<Map<String, Object>> uploadParkingImages(
            @Parameter(description = "Parking image files (max 3)", required = true)
            @RequestParam("files") List<MultipartFile> files) {
        
        try {
            List<String> imageUrls = fileStorageService.storeMultipleFiles(files, "parking-images", 3);
            
            Map<String, Object> response = new HashMap<>();
            response.put("imageUrls", imageUrls);
            response.put("message", "Parking images uploaded successfully");
            response.put("count", imageUrls.size());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error uploading parking images: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Upload single image", description = "Upload a single image file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid file or file size exceeds limit"),
        @ApiResponse(responseCode = "500", description = "Error uploading file")
    })
    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadImage(
            @Parameter(description = "Image file", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Subfolder for organization", required = false)
            @RequestParam(value = "folder", defaultValue = "general") String folder) {
        
        try {
            String imageUrl = fileStorageService.storeFile(file, folder);
            
            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", imageUrl);
            response.put("message", "Image uploaded successfully");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error uploading image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Delete image", description = "Delete an image by URL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Image deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid URL"),
        @ApiResponse(responseCode = "500", description = "Error deleting image")
    })
    @DeleteMapping("/image")
    public ResponseEntity<Map<String, String>> deleteImage(
            @Parameter(description = "Image URL to delete", required = true)
            @RequestParam("url") String imageUrl) {
        
        try {
            System.out.println("=== DELETE IMAGE REQUEST ===");
            System.out.println("Image URL to delete: " + imageUrl);
            
            fileStorageService.deleteFile(imageUrl);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Image deleted successfully");
            response.put("deletedUrl", imageUrl);
            
            System.out.println("✅ Image deletion completed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error deleting image: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error deleting image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
