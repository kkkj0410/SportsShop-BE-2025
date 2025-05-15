package com.teamproject.back.security.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.teamproject.back.util.GcsImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;

@Configuration
@Slf4j
public class GcsConfig {

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String location;


    @Bean
    public Storage storage(){
        
        GoogleCredentials credentials = null;
        try{
            credentials = GoogleCredentials.fromStream(new FileInputStream(location));
        } catch (Exception e){
            log.error("이미지 인증 정보 생성 실패");
            return null;
        }
        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build()
                .getService();
    }
    
}
