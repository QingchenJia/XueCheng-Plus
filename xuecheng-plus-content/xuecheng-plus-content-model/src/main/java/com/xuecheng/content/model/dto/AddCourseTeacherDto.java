package com.xuecheng.content.model.dto;

import lombok.Data;

@Data
public class AddCourseTeacherDto {
    private Long courseId;

    private String teacherName;

    private String position;

    private String introduction;

    private String photograph;
}
