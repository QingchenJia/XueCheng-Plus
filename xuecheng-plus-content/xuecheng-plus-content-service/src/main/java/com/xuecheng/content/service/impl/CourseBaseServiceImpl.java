package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.model.PageParam;
import com.xuecheng.base.model.Result;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {
    /**
     * 根据分页参数和查询条件获取课程列表
     *
     * @param pageParam            分页参数，包括页码和每页大小
     * @param queryCourseParamsDto 查询课程的参数，包括课程名称、审核状态和发布状态
     * @return 返回一个包含课程列表的分页结果对象
     */
    @Override
    public Page<CourseBase> listPage(PageParam pageParam, QueryCourseParamsDto queryCourseParamsDto) {
        // 创建课程基础信息的分页对象，设置页码和每页大小
        Page<CourseBase> courseBasePage = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());

        // 获取查询课程的参数
        String courseName = queryCourseParamsDto.getCourseName();
        String auditStatus = queryCourseParamsDto.getAuditStatus();
        String publishStatus = queryCourseParamsDto.getPublishStatus();

        // 创建Lambda查询包装器，用于构建查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();

        // 根据课程名称、审核状态和发布状态构建查询条件
        queryWrapper.like(StringUtils.isNotBlank(courseName), CourseBase::getName, courseName)
                    .eq(StringUtils.isNotBlank(auditStatus), CourseBase::getAuditStatus, auditStatus)
                    .eq(StringUtils.isNotBlank(publishStatus), CourseBase::getStatus, publishStatus);

        // 执行分页查询
        page(courseBasePage, queryWrapper);

        // 返回分页结果对象
        return courseBasePage;
    }
}
