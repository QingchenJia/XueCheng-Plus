package com.xuecheng.content.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("course_category")
public class CourseCategory implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String label;

    private String parentId;

    private Integer isShow;

    private Integer orderBy;

    private Integer isLeaf;
}
