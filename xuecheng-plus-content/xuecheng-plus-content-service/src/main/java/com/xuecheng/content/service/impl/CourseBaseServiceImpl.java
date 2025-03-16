package com.xuecheng.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.model.PageParam;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.CourseCategoryService;
import com.xuecheng.content.service.CourseMarketService;
import com.xuecheng.system.constant.SystemConstant;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {
    @Resource
    private CourseMarketService courseMarketService;

    @Resource
    private CourseCategoryService courseCategoryService;

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
        queryWrapper.like(StrUtil.isNotBlank(courseName), CourseBase::getName, courseName)
                .eq(StrUtil.isNotBlank(auditStatus), CourseBase::getAuditStatus, auditStatus)
                .eq(StrUtil.isNotBlank(publishStatus), CourseBase::getStatus, publishStatus);

        // 执行分页查询
        page(courseBasePage, queryWrapper);

        // 返回分页结果对象
        return courseBasePage;
    }

    @Override
    public CourseBaseDto saveInfo(AddCourseDto addCourseDto) {
        Long companyId = 1L;

        if (StrUtil.isBlank(addCourseDto.getName())) {
            throw new RuntimeException("课程名称为空");
        }

        if (StrUtil.isBlank(addCourseDto.getMt())) {
            throw new RuntimeException("课程分类为空");
        }

        if (StrUtil.isBlank(addCourseDto.getSt())) {
            throw new RuntimeException("课程分类为空");
        }

        if (StrUtil.isBlank(addCourseDto.getGrade())) {
            throw new RuntimeException("课程等级为空");
        }

        if (StrUtil.isBlank(addCourseDto.getTeachMode())) {
            throw new RuntimeException("教育模式为空");
        }

        if (StrUtil.isBlank(addCourseDto.getUsers())) {
            throw new RuntimeException("适应人群为空");
        }

        if (StrUtil.isBlank(addCourseDto.getCharge())) {
            throw new RuntimeException("收费规则为空");
        }

        CourseBase courseBase = BeanUtil.copyProperties(addCourseDto, CourseBase.class);
        courseBase.setCompanyId(companyId);
        courseBase.setAuditStatus(SystemConstant.COURSE_AUDIT_NO);
        courseBase.setStatus(SystemConstant.COURSE_PUBLISH_NO);

        save(courseBase);

        CourseMarket courseMarket = BeanUtil.copyProperties(addCourseDto, CourseMarket.class);
        courseMarket.setId(courseBase.getId());

        String charge = courseMarket.getCharge();
        if (StrUtil.isBlank(charge)) {
            throw new RuntimeException("收费规则没有选择");
        }

        if (charge.equals(SystemConstant.COURSE_CHARGE)) {
            if (courseMarket.getPrice() == null || courseMarket.getPrice() <= 0) {
                throw new RuntimeException("课程为收费价格不能为空且必须大于0");
            }
        }

        courseMarketService.save(courseMarket);

        CourseBaseDto courseBaseDto = BeanUtil.copyProperties(courseMarket, CourseBaseDto.class);

        CourseCategory mtCourseCategory = courseCategoryService.getById(courseBase.getMt());
        CourseCategory stCourseCategory = courseCategoryService.getById(courseBase.getSt());
        courseBaseDto.setMtName(mtCourseCategory.getName());
        courseBaseDto.setStName(stCourseCategory.getName());

        return courseBaseDto;
    }
}
