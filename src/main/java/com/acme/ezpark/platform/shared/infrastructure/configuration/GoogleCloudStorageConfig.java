package com.acme.ezpark.platform.shared.infrastructure.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class GoogleCloudStorageConfig {

    @Value("${google.cloud.storage.project-id}")
    private String projectId;

    @Value("${google.cloud.storage.credentials.location:}")
    private String credentialsLocation;

    @Value("${google.cloud.storage.enabled:false}")
    private boolean gcsEnabled;

    @Bean
    @Profile("!test")
    public Storage googleCloudStorage() throws IOException {
        if (!gcsEnabled) {
            System.out.println("Google Cloud Storage is disabled");
            return null;
        }

        GoogleCredentials credentials;
        
        try {
            // Try to get credentials from environment variable first (for Render deployment)
            String credentialsJson = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON");
            if (credentialsJson != null && !credentialsJson.isEmpty()) {
                System.out.println("Using Google Cloud credentials from environment variable");
                credentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8))
                );
            } else if (credentialsLocation != null && !credentialsLocation.isEmpty()) {
                // Try to load from file
                System.out.println("Using Google Cloud credentials from file: " + credentialsLocation);
                credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsLocation));
            } else {
                // Fallback to default credentials (for local development with gcloud CLI)
                System.out.println("Using default Google Cloud credentials");
                credentials = GoogleCredentials.getApplicationDefault();
            }
        } catch (Exception e) {
            System.err.println("Failed to load Google Cloud credentials: " + e.getMessage());
            System.err.println("Google Cloud Storage will be disabled");
            return null;
        }

        try {
            Storage storage = StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(credentials)
                    .build()
                    .getService();
            
            System.out.println("Google Cloud Storage initialized successfully for project: " + projectId);
            return storage;
        } catch (Exception e) {
            System.err.println("Failed to initialize Google Cloud Storage: " + e.getMessage());
            return null;
        }
    }
}
