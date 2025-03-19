package com.xuecheng.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.CustomException;
import com.xuecheng.content.mapper.TeachPlanMediaMapper;
import com.xuecheng.content.model.dto.BindTeachPlanMediaDto;
import com.xuecheng.content.model.po.TeachPlan;
import com.xuecheng.content.model.po.TeachPlanMedia;
import com.xuecheng.content.service.TeachPlanMediaService;
import com.xuecheng.content.service.TeachPlanService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeachPlanMediaServiceImpl extends ServiceImpl<TeachPlanMediaMapper, TeachPlanMedia> implements TeachPlanMediaService {
    @Resource
    @Lazy
    private TeachPlanService teachPlanService;

    /**
     * 绑定教学计划与媒体资源
     * <p>
     * 该方法用于将媒体资源绑定到特定的教学计划中，确保每个教学计划可以关联到相应的媒体文件
     * 它首先验证传入的教学计划ID、媒体资源ID和文件名是否有效，然后移除已存在的关联记录，
     * 最后创建新的关联记录并保存到数据库中
     *
     * @param bindTeachPlanMediaDto 包含教学计划与媒体资源绑定信息的数据传输对象
     * @throws CustomException 如果教学计划ID、媒体资源ID或文件名为空，则抛出自定义异常
     */
    @Override
    @Transactional
    public void bind(BindTeachPlanMediaDto bindTeachPlanMediaDto) {
        // 获取教学计划ID、媒体资源ID和文件名
        Long teachPlanId = bindTeachPlanMediaDto.getTeachPlanId();
        String mediaId = bindTeachPlanMediaDto.getMediaId();
        String filename = bindTeachPlanMediaDto.getFilename();

        // 验证教学计划ID是否为空
        if (teachPlanId == null) {
            throw new CustomException("课程计划id为空");
        }

        // 验证媒体资源ID是否为空
        if (StrUtil.isBlank(mediaId)) {
            throw new CustomException("媒体资源文件id为空");
        }

        // 验证文件名是否为空
        if (StrUtil.isBlank(filename)) {
            throw new CustomException("媒体资源文件名称为空");
        }

        // 创建查询包装器，用于构建查询条件
        LambdaQueryWrapper<TeachPlanMedia> queryWrapper = new LambdaQueryWrapper<>();
        // 设置查询条件：教学计划ID等于传入的教学计划ID
        queryWrapper.eq(TeachPlanMedia::getTeachPlanId, teachPlanId);
        // 移除已存在的关联记录，确保教学计划可以绑定到新的媒体资源
        remove(queryWrapper);

        // 将绑定信息复制到新的TeachPlanMedia对象中
        TeachPlanMedia teachPlanMedia = BeanUtil.copyProperties(bindTeachPlanMediaDto, TeachPlanMedia.class);

        // 获取教学计划对象
        TeachPlan teachPlan = teachPlanService.getById(teachPlanId);
        // 设置课程ID
        teachPlanMedia.setCourseId(teachPlan.getCourseId());
        // 设置媒体资源文件名
        teachPlanMedia.setMediaFilename(filename);

        // 保存新的关联记录
        save(teachPlanMedia);
    }
}
