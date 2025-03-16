package com.xuecheng.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.CustomException;
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
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 保存课程信息
     *
     * @param addCourseDto 用于添加课程的数据传输对象
     * @return 返回保存后的课程基本信息DTO
     * @throws CustomException 如果课程信息不完整或不合规，则抛出自定义异常
     */
    @Override
    @Transactional
    public CourseBaseDto saveInfo(AddCourseDto addCourseDto) {
        // 默认公司ID，用于关联课程与公司
        Long companyId = 1L;

        // 校验课程名称是否为空
        if (StrUtil.isBlank(addCourseDto.getName())) {
            throw new CustomException("课程名称为空");
        }

        // 校验课程主分类是否为空
        if (StrUtil.isBlank(addCourseDto.getMt())) {
            throw new CustomException("课程分类为空");
        }

        // 校验课程子分类是否为空
        if (StrUtil.isBlank(addCourseDto.getSt())) {
            throw new CustomException("课程分类为空");
        }

        // 校验课程等级是否为空
        if (StrUtil.isBlank(addCourseDto.getGrade())) {
            throw new CustomException("课程等级为空");
        }

        // 校验教育模式是否为空
        if (StrUtil.isBlank(addCourseDto.getTeachMode())) {
            throw new CustomException("教育模式为空");
        }

        // 校验适应人群是否为空
        if (StrUtil.isBlank(addCourseDto.getUsers())) {
            throw new CustomException("适应人群为空");
        }

        // 校验收费规则是否为空
        if (StrUtil.isBlank(addCourseDto.getCharge())) {
            throw new CustomException("收费规则为空");
        }

        // 将添加课程的DTO转换为课程基础实体对象
        CourseBase courseBase = BeanUtil.copyProperties(addCourseDto, CourseBase.class);
        // 设置课程所属公司ID
        courseBase.setCompanyId(companyId);
        // 设置课程审核状态为未审核
        courseBase.setAuditStatus(SystemConstant.COURSE_AUDIT_NO);
        // 设置课程发布状态为未发布
        courseBase.setStatus(SystemConstant.COURSE_PUBLISH_NO);

        // 保存课程基础信息
        save(courseBase);

        // 将添加课程的DTO转换为课程市场实体对象
        CourseMarket courseMarket = BeanUtil.copyProperties(addCourseDto, CourseMarket.class);
        // 设置课程市场信息的ID为课程基础信息的ID
        courseMarket.setId(courseBase.getId());

        // 再次校验收费规则是否为空
        String charge = courseMarket.getCharge();
        if (StrUtil.isBlank(charge)) {
            throw new CustomException("收费规则没有选择");
        }

        // 如果课程为收费，校验课程价格是否合规
        if (charge.equals(SystemConstant.COURSE_CHARGE)) {
            if (courseMarket.getPrice() == null || courseMarket.getPrice() <= 0) {
                throw new CustomException("课程为收费价格不能为空且必须大于0");
            }
        }

        // 保存课程市场信息
        courseMarketService.save(courseMarket);

        // 将课程市场信息转换为课程基础DTO
        CourseBaseDto courseBaseDto = BeanUtil.copyProperties(courseMarket, CourseBaseDto.class);

        // 根据课程主分类ID获取课程主分类信息
        CourseCategory mtCourseCategory = courseCategoryService.getById(courseBase.getMt());
        // 根据课程子分类ID获取课程子分类信息
        CourseCategory stCourseCategory = courseCategoryService.getById(courseBase.getSt());
        // 设置课程基础DTO的主分类名称
        courseBaseDto.setMtName(mtCourseCategory.getName());
        // 设置课程基础DTO的子分类名称
        courseBaseDto.setStName(stCourseCategory.getName());

        // 返回保存后的课程基础信息DTO
        return courseBaseDto;
    }
}
