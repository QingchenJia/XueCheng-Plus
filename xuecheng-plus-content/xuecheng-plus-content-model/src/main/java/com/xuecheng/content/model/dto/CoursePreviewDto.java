package com.xuecheng.content.model.dto;

import cn.hutool.core.lang.tree.Tree;
import lombok.Data;

import java.util.List;

@Data
public class CoursePreviewDto {
    private CourseBaseDto courseBaseDto;

    private List<Tree<String>> teachPlanTree;
}
