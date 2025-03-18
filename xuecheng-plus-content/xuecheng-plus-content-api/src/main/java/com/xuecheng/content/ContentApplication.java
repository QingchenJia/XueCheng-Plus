package com.xuecheng.content;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.xuecheng.content", "com.xuecheng.base.config"})
@Slf4j
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
        log.info("\nApi-Doc:\t{}", "http://localhost:63040/doc.html");
    }
}
