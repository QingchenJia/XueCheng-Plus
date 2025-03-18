package com.xuecheng.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.CustomException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.dto.AddCourseTeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherService {
    /**
     * 保存课程教师信息
     * <p>
     * 此方法用于将添加课程教师的数据传输对象中的信息保存到数据库中在执行保存操作之前，方法会检查数据传输对象中的必填字段如果任何字段为空，
     * 则抛出自定义异常，指明缺少的字段信息如果所有检查通过，则将数据传输对象转换为实体对象，并调用保存方法
     *
     * @param addCourseTeacherDto 添加课程教师的数据传输对象，包含要保存的课程教师信息
     * @return 返回保存后的课程教师实体对象
     * @throws CustomException 如果课程ID、教师名称、介绍、职位或照片为空，则抛出自定义异常
     */
    @Override
    public CourseTeacher saveInfo(AddCourseTeacherDto addCourseTeacherDto) {
        // 检查课程ID是否为空
        if (addCourseTeacherDto.getCourseId() == null) {
            throw new CustomException("课程ID为空");
        }

        // 检查教师名称是否为空
        if (StrUtil.isBlank(addCourseTeacherDto.getTeacherName())) {
            throw new CustomException("教师名称为空");
        }

        // 检查教师介绍是否为空
        if (StrUtil.isBlank(addCourseTeacherDto.getIntroduction())) {
            throw new CustomException("教师介绍为空");
        }

        // 检查教师职位是否为空
        if (StrUtil.isBlank(addCourseTeacherDto.getPosition())) {
            throw new CustomException("教师职位为空");
        }

        // 检查教师照片是否为空
        if (StrUtil.isBlank(addCourseTeacherDto.getPhotograph())) {
            throw new CustomException("教师照片为空");
        }

        // 将数据传输对象转换为实体对象
        CourseTeacher courseTeacher = BeanUtil.copyProperties(addCourseTeacherDto, CourseTeacher.class);
        // 保存实体对象
        save(courseTeacher);

        // 返回保存后的实体对象
        return getById(courseTeacher.getId());
    }

    /**
     * 更新课程教师信息
     * <p>
     * 该方法首先检查课程教师对象中的各个字段是否为空，包括课程ID、教师名称、教师介绍、教师职位和教师照片
     * 如果任一字段为空，则抛出CustomException异常，提示相应的字段为空信息
     * 如果所有字段均不为空，则调用updateById方法更新数据库中的课程教师信息，并通过getById方法返回更新后的课程教师对象
     *
     * @param courseTeacher 需要更新的课程教师对象，包含要更新的课程教师信息
     * @return 返回更新后的课程教师对象
     * @throws CustomException 如果课程教师对象中的任一字段为空，则抛出此异常
     */
    @Override
    public CourseTeacher updateInfo(CourseTeacher courseTeacher) {
        // 检查课程ID是否为空
        if (courseTeacher.getCourseId() == null) {
            throw new CustomException("课程ID为空");
        }

        // 检查教师名称是否为空
        if (StrUtil.isBlank(courseTeacher.getTeacherName())) {
            throw new CustomException("教师名称为空");
        }

        // 检查教师介绍是否为空
        if (StrUtil.isBlank(courseTeacher.getIntroduction())) {
            throw new CustomException("教师介绍为空");
        }

        // 检查教师职位是否为空
        if (StrUtil.isBlank(courseTeacher.getPosition())) {
            throw new CustomException("教师职位为空");
        }

        // 检查教师照片是否为空
        if (StrUtil.isBlank(courseTeacher.getPhotograph())) {
            throw new CustomException("教师照片为空");
        }

        // 更新课程教师信息
        updateById(courseTeacher);

        // 返回更新后的课程教师对象
        return getById(courseTeacher.getId());
    }

    /**
     * 删除课程信息
     * <p>
     * 此方法首先根据课程ID查询相关的课程教师信息，如果找不到相关课程，
     * 则抛出课程信息不存在的异常如果课程存在，但指定的教师ID不在课程教师列表中，
     * 则抛出当前课程无对应id的授课教师的异常最后，根据指定的教师ID删除教师信息
     *
     * @param courseId 课程ID，用于查询和验证课程信息
     * @param id       教师ID，指定要删除的教师信息的ID
     * @throws CustomException 如果课程信息不存在或指定的教师ID不属于当前课程，抛出自定义异常
     */
    @Override
    public void deleteInfo(Long courseId, Long id) {
        // 创建查询条件，根据课程ID查询课程教师信息
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        List<CourseTeacher> courseTeachers = list(queryWrapper);

        // 检查查询结果是否为空，如果为空则抛出课程信息不存在的异常
        if (CollUtil.isEmpty(courseTeachers)) {
            throw new CustomException("课程信息不存在");
        }

        // 将查询到的课程教师信息的ID提取到列表中，用于后续的验证
        List<Long> courseTeacherIds = courseTeachers.stream()
                .map(CourseTeacher::getId)
                .toList();

        // 验证指定的教师ID是否在课程教师ID列表中，如果不在则抛出异常
        if (!courseTeacherIds.contains(id)) {
            throw new CustomException("当前课程无对应id的授课教师");
        }

        // 根据指定的教师ID删除教师信息
        removeById(id);
    }
}
