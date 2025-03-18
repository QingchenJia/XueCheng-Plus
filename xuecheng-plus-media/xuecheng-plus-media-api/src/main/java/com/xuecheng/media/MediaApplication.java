package com.xuecheng.media;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.xuecheng.media", "com.xuecheng.base.config"})
@Slf4j
public class MediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediaApplication.class, args);
        log.info("\nApi-Doc:\t{}", "http://localhost:63060/doc.html");
    }
}
