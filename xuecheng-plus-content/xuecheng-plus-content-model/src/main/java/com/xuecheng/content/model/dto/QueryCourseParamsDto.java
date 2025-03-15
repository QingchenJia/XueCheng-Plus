package com.xuecheng.content.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryCourseParamsDto {
    private String auditStatus;

    private String courseName;

    private String publishStatus;
}
