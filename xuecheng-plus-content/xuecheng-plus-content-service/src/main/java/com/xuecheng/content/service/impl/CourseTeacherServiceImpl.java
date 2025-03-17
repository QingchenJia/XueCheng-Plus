package com.xuecheng.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.CustomException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.dto.AddCourseTeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import org.springframework.stereotype.Service;

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
}
