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
import com.xuecheng.content.model.dto.EditCourseDto;
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

        // 返回保存后的课程基础信息DTO
        return getCourseInfoById(courseBase.getId());
    }

    /**
     * 根据课程ID获取课程信息
     * 此方法通过聚合课程基本信息和市场信息，以及课程的一级和二级分类信息，来构建一个课程数据传输对象
     *
     * @param courseId 课程ID，用于查询课程信息
     * @return CourseBaseDto 包含课程基本信息、市场信息及分类信息的课程数据传输对象
     */
    @Override
    public CourseBaseDto getCourseInfoById(Long courseId) {
        // 获取课程基本信息
        CourseBase courseBase = getById(courseId);
        // 获取课程市场信息
        CourseMarket courseMarket = courseMarketService.getById(courseId);

        // 将课程基本信息复制到课程数据传输对象中
        CourseBaseDto courseBaseDto = BeanUtil.copyProperties(courseBase, CourseBaseDto.class);
        // 将课程市场信息复制到课程数据传输对象中
        BeanUtil.copyProperties(courseMarket, courseBaseDto);

        // 获取课程的一级分类信息
        CourseCategory mtCourseCategory = courseCategoryService.getById(courseBase.getMt());
        // 获取课程的二级分类信息
        CourseCategory stCourseCategory = courseCategoryService.getById(courseBase.getSt());

        // 设置课程数据传输对象的一级分类名称
        courseBaseDto.setMtName(mtCourseCategory.getName());
        // 设置课程数据传输对象的二级分类名称
        courseBaseDto.setStName(stCourseCategory.getName());

        // 返回包含完整课程信息的课程数据传输对象
        return courseBaseDto;
    }

    /**
     * 更新课程信息
     * <p>
     * 此方法主要用于更新课程的基本信息和市场信息它首先验证了课程名称、分类、等级、教育模式、适应人群和收费规则等字段是否填写
     * 然后将编辑课程的DTO对象转换为CourseBase对象，并设置课程的审核状态和发布状态之后，更新课程基本信息
     * 接着，将DTO对象转换为CourseMarket对象，再次验证收费规则，并根据收费规则检查课程价格最后，更新课程市场信息
     *
     * @param editCourseDto 包含要编辑课程信息的数据传输对象
     * @return 返回更新后的课程基本信息DTO对象
     * @throws CustomException 如果课程信息填写不完整或价格不符合要求，抛出自定义异常
     */
    @Override
    public CourseBaseDto updateInfo(EditCourseDto editCourseDto) {
        // 检查课程名称是否为空
        if (StrUtil.isBlank(editCourseDto.getName())) {
            throw new CustomException("课程名称为空");
        }

        // 检查课程主分类是否为空
        if (StrUtil.isBlank(editCourseDto.getMt())) {
            throw new CustomException("课程分类为空");
        }

        // 检查课程子分类是否为空
        if (StrUtil.isBlank(editCourseDto.getSt())) {
            throw new CustomException("课程分类为空");
        }

        // 检查课程等级是否为空
        if (StrUtil.isBlank(editCourseDto.getGrade())) {
            throw new CustomException("课程等级为空");
        }

        // 检查教育模式是否为空
        if (StrUtil.isBlank(editCourseDto.getTeachMode())) {
            throw new CustomException("教育模式为空");
        }

        // 检查适应人群是否为空
        if (StrUtil.isBlank(editCourseDto.getUsers())) {
            throw new CustomException("适应人群为空");
        }

        // 检查收费规则是否为空
        if (StrUtil.isBlank(editCourseDto.getCharge())) {
            throw new CustomException("收费规则为空");
        }

        // 将EditCourseDto对象转换为CourseBase对象
        CourseBase courseBase = BeanUtil.copyProperties(editCourseDto, CourseBase.class);

        // 设置课程的审核状态为未审核
        courseBase.setAuditStatus(SystemConstant.COURSE_AUDIT_NO);
        // 设置课程的发布状态为未发布
        courseBase.setStatus(SystemConstant.COURSE_PUBLISH_NO);

        // 更新课程基本信息
        updateById(courseBase);

        // 将EditCourseDto对象转换为CourseMarket对象
        CourseMarket courseMarket = BeanUtil.copyProperties(editCourseDto, CourseMarket.class);

        // 再次检查收费规则是否为空
        String charge = courseMarket.getCharge();
        if (StrUtil.isBlank(charge)) {
            throw new CustomException("收费规则没有选择");
        }

        // 如果课程为收费，检查价格是否有效
        if (charge.equals(SystemConstant.COURSE_CHARGE)) {
            if (courseMarket.getPrice() == null || courseMarket.getPrice() <= 0) {
                throw new CustomException("课程为收费价格不能为空且必须大于0");
            }
        }

        // 更新课程市场信息
        courseMarketService.updateById(courseMarket);

        // 根据课程ID返回更新后的课程信息
        return getCourseInfoById(editCourseDto.getId());
    }

    /**
     * 根据ID删除课程信息
     * 此方法首先检查课程的审核状态，如果课程已提交审核，则不允许删除
     * 如果课程未提交审核，则同时删除课程基础信息和市场信息
     *
     * @param id 课程ID，用于定位要删除的课程信息
     * @throws CustomException 如果课程已提交受审，抛出自定义异常阻止删除操作
     */
    @Override
    public void deleteInfo(Long id) {
        // 根据ID获取课程基础信息
        CourseBase courseBase = getById(id);
        // 检查课程的审核状态，如果课程已提交审核，则不允许删除
        if (!courseBase.getAuditStatus().equals(SystemConstant.COURSE_AUDIT_NO)) {
            throw new CustomException("课程已提交受审，不能删除");
        }

        // 删除课程基础信息
        removeById(id);

        // 删除与课程关联的市场信息
        courseMarketService.removeById(id);
    }
}
