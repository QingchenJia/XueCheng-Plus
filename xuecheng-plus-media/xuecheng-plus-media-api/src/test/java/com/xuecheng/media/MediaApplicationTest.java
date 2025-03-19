package com.xuecheng.media;

import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest
public class MediaApplicationTest {
    @Resource
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Test
    void uploadFileToMinio() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MultipartFile file = new MockMultipartFile("文件上传测试1.png", "wallhaven-rr3y61_3840x2160.png", "image/jpeg", new FileInputStream("D:\\Acollection\\锁屏壁纸\\wallhaven-rr3y61_3840x2160.png"));

        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                .bucket(bucketName)
                .build();

        boolean bucketExists = minioClient.bucketExists(bucketExistsArgs);

        if (!bucketExists) {
            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build();

            minioClient.makeBucket(makeBucketArgs);

            SetBucketPolicyArgs setBucketPolicyArgs = SetBucketPolicyArgs.builder()
                    .bucket(bucketName)
                    .config("""
                            {
                              "Statement" : [ {
                                "Action" : "s3:GetObject",
                                "Effect" : "Allow",
                                "Principal" : "*",
                                "Resource" : "arn:aws:s3:::%s/*"
                              } ],
                              "Version" : "2012-10-17"
                            }
                            """.formatted(bucketName))
                    .build();

            minioClient.setBucketPolicy(setBucketPolicyArgs);
        }

        String filename = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "/" + file.getOriginalFilename();

        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .contentType(file.getContentType())
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(filename)
                .build();

        minioClient.putObject(putObjectArgs);
    }

    @Test
    void downloadFileFromMinio() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        DownloadObjectArgs downloadObjectArgs = DownloadObjectArgs.builder()
                .bucket(bucketName)
                .filename("C:\\Users\\87948\\Desktop\\文件下载测试1.png")
                .object("2025-03-18/wallhaven-rr3y61_3840x2160.png")
                .overwrite(true)
                .build();

        minioClient.downloadObject(downloadObjectArgs);
    }

    @Test
    void parseFileContentType() throws IOException {
        MultipartFile file = new MockMultipartFile("文件上传测试1.png", "wallhaven-rr3y61_3840x2160.png", "application/octet-stream", new FileInputStream("D:\\Acollection\\锁屏壁纸\\wallhaven-rr3y61_3840x2160.png"));

        Path tempFile = Files.createTempFile("temp", file.getOriginalFilename());
        file.transferTo(tempFile.toAbsolutePath());

        String contentType = Files.probeContentType(tempFile);

        Files.delete(tempFile);

        System.out.println(contentType);
    }
}
