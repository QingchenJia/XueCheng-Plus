package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.PageParam;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

public interface CourseBaseService extends IService<CourseBase> {
    Page<CourseBase> listPage(PageParam pageParam, QueryCourseParamsDto queryCourseParamsDto);

    CourseBaseDto saveInfo(AddCourseDto addCourseDto);

    CourseBaseDto getCourseInfoById(Long courseId);

    CourseBaseDto updateInfo(EditCourseDto editCourseDto);
}
