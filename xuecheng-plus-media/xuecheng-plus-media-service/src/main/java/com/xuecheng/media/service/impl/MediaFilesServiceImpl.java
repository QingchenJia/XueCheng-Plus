package com.xuecheng.media.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.CustomException;
import com.xuecheng.base.model.PageParam;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.model.dto.QueryMediaParamDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFilesService;
import com.xuecheng.system.constant.SystemConstant;
import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class MediaFilesServiceImpl extends ServiceImpl<MediaFilesMapper, MediaFiles> implements MediaFilesService {
    @Resource
    private MinioClient minioClient;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.bucket-name}")
    private String bucketName;

    /**
     * 根据分页参数和查询条件列出媒体文件
     *
     * @param pageParam          分页参数，包含页码和每页大小
     * @param queryMediaParamDto 查询媒体文件的参数，包括文件类型、文件名和审核状态
     * @return 返回一个分页对象，包含查询到的媒体文件列表
     */
    @Override
    public Page<MediaFiles> listPage(PageParam pageParam, QueryMediaParamDto queryMediaParamDto) {
        // 初始化分页对象
        Page<MediaFiles> mediaFilesPage = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());

        // 获取查询条件
        String fileType = queryMediaParamDto.getFileType();
        String filename = queryMediaParamDto.getFilename();
        String auditStatus = queryMediaParamDto.getAuditStatus();

        // 创建查询封装对象，用于后续的条件查询
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
        // 根据查询条件构建查询封装对象
        queryWrapper.like(StrUtil.isNotBlank(fileType), MediaFiles::getFileType, fileType)
                .like(StrUtil.isNotBlank(filename), MediaFiles::getFilename, filename)
                .eq(StrUtil.isNotBlank(auditStatus), MediaFiles::getAuditStatus, auditStatus);

        // 执行分页查询
        page(mediaFilesPage, queryWrapper);

        // 返回查询到的分页结果
        return mediaFilesPage;
    }

    /**
     * 上传媒体文件到MinIO服务器
     * <p>
     * 该方法负责将multipart文件上传到配置的MinIO bucket中，并设置相应的策略以允许公开访问
     * 它首先检查目标bucket是否存在，如果不存在则创建bucket并设置bucket策略
     * 然后，它计算文件的MD5哈希值作为文件ID，并根据当前日期和文件ID生成存储路径
     * 最后，将文件内容上传到MinIO，并保存文件信息到数据库
     *
     * @param multipartFile 要上传的multipart文件
     * @return 保存的文件信息对象
     * @throws ServerException 如服务器异常、数据不足异常、错误响应异常等
     */
    @Override
    public MediaFiles uploadMediaFile(MultipartFile multipartFile) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 检查bucket是否存在
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                .bucket(bucketName)
                .build();
        boolean bucketExists = minioClient.bucketExists(bucketExistsArgs);

        if (!bucketExists) {
            // 如果bucket不存在，创建新的bucket
            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build();
            minioClient.makeBucket(makeBucketArgs);

            // 设置bucket策略，允许公开访问
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

        // 计算文件的MD5哈希值作为文件ID
        String fileId = DigestUtil.md5Hex(multipartFile.getBytes());
        // 根据当前日期和文件ID生成存储路径
        String filename = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                + "/" + fileId;

        // 解析文件内容类型
        String contentType = parseFileContentType(multipartFile);
        // 准备上传文件到MinIO
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .contentType(contentType)
                .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                .object(filename)
                .build();
        minioClient.putObject(putObjectArgs);

        // 保存文件信息到数据库
        return saveFileInfo(multipartFile, contentType, filename);
    }

    /**
     * 根据媒体资源文件的ID获取其URL
     *
     * @param mediaId 媒体资源文件的唯一标识符
     * @return 媒体资源文件的访问URL
     * @throws CustomException 如果媒体资源文件不存在或其URL为空时抛出自定义异常
     */
    @Override
    public String getUrl(String mediaId) {
        // 通过ID获取媒体资源文件对象
        MediaFiles mediaFiles = getById(mediaId);

        // 检查媒体资源文件是否存在，如果不存在则抛出异常
        if (mediaFiles == null) {
            throw new CustomException("该媒体资源文件不存在");
        }

        // 获取媒体资源文件的URL
        String url = mediaFiles.getUrl();

        // 检查URL是否为空，如果为空则抛出异常
        if (StrUtil.isBlank(url)) {
            throw new CustomException("当前媒体资源文件处理为完毕");
        }

        // 返回媒体资源文件的URL
        return url;
    }

    /**
     * 保存文件信息到数据库
     *
     * @param multipartFile 文件对象，用于获取文件的原始名称和内容类型
     * @param contentType   文件的内容类型，用于解析文件类型
     * @param filename      文件名，用于构造文件路径
     * @return 保存后的MediaFiles对象
     */
    private MediaFiles saveFileInfo(MultipartFile multipartFile, String contentType, String filename) {
        // 创建MediaFiles对象并设置其属性
        MediaFiles mediaFiles = new MediaFiles();
        // 使用文件原始名称的MD5值作为文件ID
        mediaFiles.setId(DigestUtil.md5Hex(multipartFile.getOriginalFilename()));
        // 设置公司ID为1
        mediaFiles.setCompanyId(1L);
        // 设置文件原始名称
        mediaFiles.setFilename(multipartFile.getOriginalFilename());
        // 解析并设置文件类型
        mediaFiles.setFileType(parseFileType(contentType));
        // 设置文件存储的桶名称
        mediaFiles.setBucket(bucketName);
        // 设置文件路径，以'/'开头
        mediaFiles.setFilePath("/" + filename);
        // 使用文件ID作为数据库的ID
        mediaFiles.setFileId(mediaFiles.getId());
        // 构造文件的URL地址
        mediaFiles.setUrl(String.join("/", endpoint, bucketName, filename));
        // 设置文件的审核状态为已提交
        mediaFiles.setAuditStatus(SystemConstant.COURSE_AUDIT_SUBMIT);
        // 设置文件的大小
        mediaFiles.setFileSize(multipartFile.getSize());

        // 保存文件信息到数据库
        save(mediaFiles);

        // 根据ID获取并返回保存后的文件信息
        return getById(mediaFiles.getId());
    }

    /**
     * 解析文件的内容类型
     * <p>
     * 由于无法直接从内存中的文件数据获取内容类型，因此需要将文件临时保存到磁盘上
     * 此方法首先创建一个临时文件，并将上传的文件内容转移到这个临时文件中
     * 然后，它使用Files.probeContentType方法来检测文件的内容类型
     * 最后，删除临时文件以释放资源，并返回检测到的内容类型
     *
     * @param multipartFile 上传的文件对象，用于获取文件内容和元数据
     * @return 文件的内容类型字符串，例如 "image/png" 或 "application/pdf"
     * @throws IOException 如果在创建临时文件、转移文件内容或检测内容类型时发生I/O错误
     */
    private String parseFileContentType(MultipartFile multipartFile) throws IOException {
        // 创建临时文件，用于解析内容类型
        Path tempFile = Files.createTempFile("temp", multipartFile.getOriginalFilename());
        // 将上传的文件内容转移到临时文件中
        multipartFile.transferTo(tempFile.toAbsolutePath());

        // 探测并获取文件的内容类型
        String contentType = Files.probeContentType(tempFile);
        // 删除临时文件，释放资源
        Files.delete(tempFile);

        // 返回文件的内容类型
        return contentType;
    }

    /**
     * 根据内容类型解析文件类型
     *
     * @param contentType 内容类型的字符串表示，用于识别文件类型
     * @return 返回文件类型对应的字符串标识：
     * - 如果内容类型包含"image"，则返回系统定义的图片资源标识
     * - 如果内容类型包含"video"，则返回系统定义的视频资源标识
     * - 如果内容类型既不包含"image"也不包含"video"，则返回系统定义的其他资源标识
     */
    private String parseFileType(String contentType) {
        // 检查内容类型是否包含"image"，以决定是否为图片资源
        if (contentType.contains("image")) {
            return SystemConstant.RESOURCE_IMAGE;
        } else if (contentType.contains("video")) {
            // 检查内容类型是否包含"video"，以决定是否为视频资源
            return SystemConstant.RESOURCE_VIDEO;
        } else {
            // 如果内容类型既不是图片也不是视频，则视为其他资源
            return SystemConstant.RESOURCE_OTHER;
        }
    }
}
