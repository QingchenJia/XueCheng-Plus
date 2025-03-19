package com.xuecheng.media.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    /**
     * 配置并创建MinioClient实例
     *
     * @return MinioClient实例，用于与Minio服务器进行通信
     */
    @Bean
    public MinioClient minioClient() {
        // 构建MinioClient实例，需要指定Minio服务器的端点、访问密钥和秘密密钥
        return MinioClient.builder()
                .endpoint(endpoint) // 设置Minio服务器的端点地址
                .credentials(accessKey, secretKey) // 设置访问Minio服务器的凭证
                .build(); // 完成MinioClient实例的构建
    }
}
