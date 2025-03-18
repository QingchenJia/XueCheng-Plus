package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.AddCourseTeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;

public interface CourseTeacherService extends IService<CourseTeacher> {
    CourseTeacher saveInfo(AddCourseTeacherDto addCourseTeacherDto);

    CourseTeacher updateInfo(CourseTeacher courseTeacher);

    void deleteInfo(Long courseId, Long id);
}
