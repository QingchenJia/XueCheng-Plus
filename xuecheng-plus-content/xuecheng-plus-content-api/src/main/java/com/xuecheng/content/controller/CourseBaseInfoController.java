package com.xuecheng.content.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParam;
import com.xuecheng.base.model.Result;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
