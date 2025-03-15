package com.xuecheng.content.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("teach_plan")
public class TeachPlan implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private Long parentId;

    private Integer grade;

    private String mediaType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String description;

    private String timeLength;

    private Integer orderBy;

    private Long courseId;

    private Long coursePubId;

    private Integer status;

    private String isPreview;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changeDate;
}
