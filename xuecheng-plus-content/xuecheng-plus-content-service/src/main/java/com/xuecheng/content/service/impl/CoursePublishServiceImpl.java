package com.xuecheng.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.CustomException;
import com.xuecheng.content.mapper.CoursePublishMapper;
import com.xuecheng.content.model.dto.CourseBaseDto;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.content.service.TeachPlanService;
import com.xuecheng.system.constant.SystemConstant;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CoursePublishServiceImpl extends ServiceImpl<CoursePublishMapper, CoursePublish> implements CoursePublishService {
    @Resource
    private CourseBaseService courseBaseService;

    @Resource
    private TeachPlanService teachPlanService;

    /**
     * 预览课程信息
     * <p>
     * 根据课程ID获取课程基本信息和教学计划树，组装成课程预览信息对象返回
     *
     * @param courseId 课程ID，用于获取特定课程的信息
     * @return CoursePreviewDto 课程预览信息对象，包含课程基本信息和教学计划树
     */
    @Override
    public CoursePreviewDto preview(Long courseId) {
        // 获取课程基本信息
        CourseBaseDto courseBaseDto = courseBaseService.getCourseInfoById(courseId);
        // 获取教学计划树
        List<Tree<String>> teachPlanTree = teachPlanService.getPlanTree(courseId);

        // 创建课程预览信息对象
        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();
        // 设置课程基本信息
        coursePreviewDto.setCourseBaseDto(courseBaseDto);
        // 设置教学计划树
        coursePreviewDto.setTeachPlanTree(teachPlanTree);

        // 返回课程预览信息对象
        return coursePreviewDto;
    }

    /**
     * 提交课程审核
     * <p>
     * 此方法用于将课程信息提交给管理员进行审核提交前会进行一系列的校验，包括课程ID的有效性、
     * 课程是否存在、课程是否已提交审核、课程是否属于当前机构、是否上传了课程图片以及是否添加了课程计划
     *
     * @param courseId 课程ID，用于指定需要提交审核的课程
     * @throws CustomException 如果课程ID为空、课程不存在、课程已提交审核、课程不属于当前机构、
     *                         没有上传课程图片或没有添加课程计划，将抛出此异常
     */
    @Override
    @Transactional
    public void commitAudit(Long courseId) {
        // 检查课程ID是否为空
        if (courseId == null) {
            throw new CustomException("课程ID为空");
        }

        // 根据课程ID获取课程基本信息
        CourseBase courseBase = courseBaseService.getById(courseId);

        // 检查课程是否存在
        if (courseBase == null) {
            throw new CustomException("课程不存在");
        }

        // 检查课程是否已提交审核
        if (courseBase.getAuditStatus().equals(SystemConstant.COURSE_AUDIT_SUBMIT)) {
            throw new CustomException("课程已提交审核，不允许重复提交");
        }

        // 检查课程是否属于当前机构
        Long company = 1L;
        if (!courseBase.getCompanyId().equals(company)) {
            throw new CustomException("本机构只允许提交本机构的课程");
        }

        // 检查是否上传了课程图片
        if (StrUtil.isBlank(courseBase.getPic())) {
            throw new CustomException("没有上传图片不允许提交审核");
        }

        // 获取课程计划树
        List<Tree<String>> teachPlanTree = teachPlanService.getPlanTree(courseId);
        // 检查是否添加了课程计划
        if (CollUtil.isEmpty(teachPlanTree)) {
            throw new CustomException("没有添加课程计划不允许提交审核");
        }

        // 根据课程ID获取课程详细信息，并准备将其转换为课程发布对象
        CourseBaseDto courseBaseDto = courseBaseService.getCourseInfoById(courseId);
        CoursePublish coursePublish = BeanUtil.copyProperties(courseBaseDto, CoursePublish.class);
        // 保存课程发布信息
        saveOrUpdate(coursePublish);

        // 更新课程审核状态为已提交
        courseBase.setAuditStatus(SystemConstant.COURSE_AUDIT_SUBMIT);
        // 更新课程基本信息
        courseBaseService.updateById(courseBase);
    }
}
