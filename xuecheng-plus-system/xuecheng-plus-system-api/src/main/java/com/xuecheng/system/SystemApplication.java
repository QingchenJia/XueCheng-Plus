package com.xuecheng.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.xuecheng.system", "com.xuecheng.base"})
@Slf4j
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
        log.info("\nApi-Doc:\t{}", "http://localhost:63050/doc.html");
    }
}
