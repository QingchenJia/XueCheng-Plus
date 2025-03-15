package com.xuecheng.content.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("course_teacher")
public class CourseTeacher implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long courseId;

    private String teacherName;

    private String position;

    private String introduction;

    private String photograph;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;
}
