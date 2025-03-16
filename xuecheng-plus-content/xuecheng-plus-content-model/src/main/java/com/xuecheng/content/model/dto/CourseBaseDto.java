package com.xuecheng.content.model.dto;

import lombok.Data;

@Data
public class CourseBaseDto {
    private String charge;

    private Float price;

    private Float originalPrice;

    private String qq;

    private String wechat;

    private String phone;

    private Integer validDays;

    private String mtName;

    private String stName;
}
