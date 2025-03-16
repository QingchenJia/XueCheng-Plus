package com.xuecheng.content.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("course_publish_pre")
public class CoursePublishPre implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long companyId;

    private String companyName;

    private String name;

    private String users;

    private String tags;

    private String username;

    private String mt;

    private String mtName;

    private String st;

    private String stName;

    private String grade;

    private String teachMode;

    private String pic;

    private String description;

    private String market;

    private String teachPlan;

    private String teachers;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    private LocalDateTime auditDate;

    private String status;

    private String remark;

    private String charge;

    private Float price;

    private Float originalPrice;

    private Integer validDays;
}
