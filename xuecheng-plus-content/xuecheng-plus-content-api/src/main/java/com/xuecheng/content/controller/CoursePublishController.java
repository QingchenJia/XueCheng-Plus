package com.xuecheng.content.controller;

import com.xuecheng.base.model.Result;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.service.CoursePublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "课程发布接口")
@RestController
@RequestMapping("/content")
public class CoursePublishController {
    @Resource
    private CoursePublishService coursePublishService;

    @Operation(summary = "课程信息预览")
    @GetMapping("/coursePreview/{courseId}")
    public Result<CoursePreviewDto> preview(@PathVariable("courseId") Long courseId) {
        CoursePreviewDto coursePreviewDto = coursePublishService.preview(courseId);
        return Result.success(coursePreviewDto);
    }


    @Operation(summary = "提交审核")
    @PostMapping("/courseAudit/commit/{courseId}")
    public Result<?> commitAudit(@PathVariable("courseId") Long courseId) {
        coursePublishService.commitAudit(courseId);
        return Result.success();
    }
}
