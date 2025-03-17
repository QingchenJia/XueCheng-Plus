package com.xuecheng.content.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParam;
import com.xuecheng.base.model.Result;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "课程信息管理接口")
@RestController
@RequestMapping("/content")
public class CourseBaseInfoController {
    @Resource
    private CourseBaseService courseBaseService;

    @Operation(summary = "课程查询接口")
    @PostMapping("/course/list")
    public Result<Page<CourseBase>> list(PageParam pageParam, @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto) {
        Page<CourseBase> courseBasePage = courseBaseService.listPage(pageParam, queryCourseParamsDto);
        return Result.success(courseBasePage);
    }

    @Operation(summary = "新增课程基础信息")
    @PostMapping("/course")
    public Result<CourseBaseDto> createCourseBase(@RequestBody AddCourseDto addCourseDto) {
        CourseBaseDto courseBaseDto = courseBaseService.saveInfo(addCourseDto);
        return Result.success(courseBaseDto);
    }

    @Operation(summary = "根据课程id查询课程基础信息")
    @GetMapping("/course/{courseId}")
    public Result<CourseBaseDto> getCourseBaseById(@PathVariable Long courseId) {
        CourseBaseDto courseBaseDto = courseBaseService.getCourseInfoById(courseId);
        return Result.success(courseBaseDto);
    }

    @Operation(summary = "修改课程基础信息")
    @PutMapping("/course")
    public Result<CourseBaseDto> modifyCourseBase(@RequestBody EditCourseDto editCourseDto) {
        CourseBaseDto courseBaseDto = courseBaseService.updateInfo(editCourseDto);
        return Result.success(courseBaseDto);
    }
}
