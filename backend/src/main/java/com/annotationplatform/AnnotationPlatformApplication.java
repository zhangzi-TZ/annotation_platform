package com.annotationplatform;

import com.annotationplatform.config.StorageProperties;
import com.annotationplatform.config.VideoPreviewProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class, VideoPreviewProperties.class})
public class AnnotationPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnnotationPlatformApplication.class, args);
    }
}
