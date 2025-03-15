package com.xuecheng.content.controller;

import com.xuecheng.base.model.Result;
import com.xuecheng.content.model.dto.CourseCategoryDto;
import com.xuecheng.content.service.CourseCategoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class CourseCategoryController {
    @Resource
    private CourseCategoryService courseCategoryService;

    @GetMapping("/category/tree")
    public Result<List<CourseCategoryDto>> queryTreeNodes() {
        List<CourseCategoryDto> courseCategoryDtos = courseCategoryService.queryTreeNodes();
        return Result.success(courseCategoryDtos);
    }
}
