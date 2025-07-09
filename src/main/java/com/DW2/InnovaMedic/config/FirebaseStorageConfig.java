package com.DW2.InnovaMedic.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;

@Configuration
public class FirebaseStorageConfig {
    @Bean
    public Storage FirebaseStorage() throws IOException{
        ClassPathResource resource = new ClassPathResource("firebase/serviceAccountKey.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
