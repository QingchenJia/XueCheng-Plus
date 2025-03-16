package com.xuecheng.content.controller;

import com.xuecheng.base.model.Result;
import com.xuecheng.content.model.dto.CourseCategoryDto;
import com.xuecheng.content.service.CourseCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "课程分类管理接口")
@RestController
@RequestMapping("/content")
public class CourseCategoryController {
    @Resource
    private CourseCategoryService courseCategoryService;

    @Operation(summary = "查询课程分类树形结构")
    @GetMapping("/category/tree")
    public Result<List<CourseCategoryDto>> queryTreeNodes() {
        List<CourseCategoryDto> courseCategoryDtos = courseCategoryService.queryTreeNodes();
        return Result.success(courseCategoryDtos);
    }
}
