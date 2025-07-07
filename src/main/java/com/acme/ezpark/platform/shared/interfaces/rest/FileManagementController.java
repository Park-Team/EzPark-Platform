package com.acme.ezpark.platform.shared.interfaces.rest;

import com.acme.ezpark.platform.shared.infrastructure.web.GoogleCloudStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "File Management", description = "Endpoints for file management and configuration")
public class FileManagementController {

    @Autowired(required = false)
    private GoogleCloudStorageService googleCloudStorageService;

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

    @Operation(summary = "Get storage info", description = "Get information about current storage configuration")
    @GetMapping("/storage-info")
    public ResponseEntity<Map<String, Object>> getStorageInfo() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean gcsEnabled = googleCloudStorageService != null && googleCloudStorageService.isEnabled();
            response.put("storageType", gcsEnabled ? "Google Cloud Storage" : "Local Storage");
            response.put("gcsEnabled", gcsEnabled);
            response.put("persistent", gcsEnabled);
            response.put("description", gcsEnabled ? 
                "Images are stored in Google Cloud Storage and will persist across deployments" : 
                "Images are stored locally and will be lost when the server restarts");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error getting storage info: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
