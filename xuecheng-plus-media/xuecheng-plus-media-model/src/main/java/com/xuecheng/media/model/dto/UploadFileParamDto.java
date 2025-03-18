package com.xuecheng.media.model.dto;

import lombok.Data;

@Data
public class UploadFileParamDto {
    private String filename;

    private String fileType;

    private Long fileSize;

    private String tags;

    private String username;

    private String remark;
}
