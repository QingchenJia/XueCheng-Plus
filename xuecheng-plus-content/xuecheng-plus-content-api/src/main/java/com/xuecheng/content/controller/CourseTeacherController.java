package com.xuecheng.content.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.model.Result;
import com.xuecheng.content.model.dto.AddCourseTeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "课程教师管理接口")
@RestController
@RequestMapping("/content")
public class CourseTeacherController {
    @Resource
    private CourseTeacherService courseTeacherService;

    @Operation(summary = "根据课程id查询授课老师")
    @GetMapping("/courseTeacher/list/{courseId}")
    public Result<List<CourseTeacher>> getTeacherInfo(@PathVariable Long courseId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        List<CourseTeacher> courseTeachers = courseTeacherService.list(queryWrapper);

        return Result.success(courseTeachers);
    }

    @Operation(summary = "保存授课老师信息")
    @PostMapping("/courseTeacher")
    public Result<CourseTeacher> saveTeacher(@RequestBody AddCourseTeacherDto addCourseTeacherDto) {
        CourseTeacher courseTeacher = courseTeacherService.saveInfo(addCourseTeacherDto);
        return Result.success(courseTeacher);
    }

    @Operation(summary = "更新授课老师信息")
    @PutMapping("/courseTeacher")
    public Result<CourseTeacher> updateTeacher(@RequestBody CourseTeacher courseTeacher) {
        CourseTeacher newCourseTeacher = courseTeacherService.updateInfo(courseTeacher);
        return Result.success(newCourseTeacher);
    }

    @Operation(summary = "删除授课老师信息")
    @DeleteMapping("/courseTeacher/course/{courseId}/{id}")
    public Result<?> deleteTeacher(@PathVariable Long courseId, @PathVariable Long id) {
        courseTeacherService.deleteInfo(courseId, id);
        return Result.success();
    }
}
