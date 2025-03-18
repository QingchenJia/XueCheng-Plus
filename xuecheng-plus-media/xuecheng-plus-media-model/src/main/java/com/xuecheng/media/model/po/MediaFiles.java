package com.xuecheng.media.model.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("media_files")
public class MediaFiles implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private Long companyId;

    private String companyName;

    private String filename;

    private String fileType;

    private String tags;

    private String bucket;

    private String filePath;

    private String fileId;

    private String url;

    private String username;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime changeDate;

    private String status;

    private String remark;

    private String auditStatus;

    private String auditMind;

    private Long fileSize;
}
