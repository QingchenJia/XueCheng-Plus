package com.xuecheng.content.model.dto;

import lombok.Data;

@Data
public class SaveOrUpdateTeachPlanDto {
    private Long id;

    private String name;

    private Long parentId;

    private Integer grade;

    private String mediaType;

    private Long courseId;

    private Long coursePubId;

    private String isPreview;
}
