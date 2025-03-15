package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.PageParam;
import com.xuecheng.base.model.Result;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

public interface CourseBaseService extends IService<CourseBase> {
    Page<CourseBase> listPage(PageParam pageParam, QueryCourseParamsDto queryCourseParamsDto);
}
