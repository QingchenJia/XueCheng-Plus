package com.xuecheng.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.TeachPlanMapper;
import com.xuecheng.content.model.dto.TeachPlanDto;
import com.xuecheng.content.model.po.TeachPlan;
import com.xuecheng.content.model.po.TeachPlanMedia;
import com.xuecheng.content.service.TeachPlanMediaService;
import com.xuecheng.content.service.TeachPlanService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachPlanServiceImpl extends ServiceImpl<TeachPlanMapper, TeachPlan> implements TeachPlanService {
    @Resource
    private TeachPlanMediaService teachPlanMediaService;

    /**
     * 获取课程计划树
     * <p>
     * 根据课程ID查询教学计划，并构建为树形结构返回
     *
     * @param courseId 课程ID
     * @return 课程计划树列表
     */
    @Override
    public List<Tree<String>> getPlanTree(Long courseId) {
        // 查询指定课程的所有教学计划
        LambdaQueryWrapper<TeachPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachPlan::getCourseId, courseId);
        List<TeachPlan> teachPlans = list(queryWrapper);

        // 将教学计划与教学计划媒体信息进行关联
        List<TeachPlanDto> teachPlanDtos = teachPlans.stream()
                .map(teachPlan -> {
                    // 查询与教学计划关联的媒体信息
                    LambdaQueryWrapper<TeachPlanMedia> teachPlanMediaQueryWrapper = new LambdaQueryWrapper<>();
                    teachPlanMediaQueryWrapper.eq(TeachPlanMedia::getTeachPlanId, teachPlan.getId());
                    TeachPlanMedia teachPlanMedia = teachPlanMediaService.getOne(teachPlanMediaQueryWrapper);

                    // 将教学计划转换为DTO，并关联媒体信息
                    TeachPlanDto teachPlanDto = BeanUtil.copyProperties(teachPlan, TeachPlanDto.class);
                    teachPlanDto.setTeachPlanMedia(teachPlanMedia);

                    return teachPlanDto;
                })
                .toList();

        // 配置树节点排序规则
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("orderBy");

        // 构建并返回课程计划树
        return TreeUtil.build(
                teachPlanDtos,
                "0",
                treeNodeConfig,
                (teachPlanDto, treeNode) -> {
                    treeNode.setId(teachPlanDto.getId().toString());
                    treeNode.setParentId(teachPlanDto.getParentId().toString());
                    treeNode.setName(teachPlanDto.getName());
                    treeNode.setWeight(teachPlanDto.getOrderBy());
                    treeNode.putExtra("grade", teachPlanDto.getGrade());
                    treeNode.putExtra("mediaType", teachPlanDto.getMediaType());
                    treeNode.putExtra("startTime", teachPlanDto.getStartTime());
                    treeNode.putExtra("endTime", teachPlanDto.getEndTime());
                    treeNode.putExtra("description", teachPlanDto.getDescription());
                    treeNode.putExtra("timeLength", teachPlanDto.getTimeLength());
                    treeNode.putExtra("courseId", teachPlanDto.getCourseId());
                    treeNode.putExtra("coursePubId", teachPlanDto.getCoursePubId());
                    treeNode.putExtra("status", teachPlanDto.getStatus());
                    treeNode.putExtra("isPreview", teachPlanDto.getIsPreview());
                    treeNode.putExtra("createDate", teachPlanDto.getCreateDate());
                    treeNode.putExtra("changeDate", teachPlanDto.getChangeDate());
                    treeNode.putExtra("teachPlanMedia", teachPlanDto.getTeachPlanMedia());
                }
        );
    }
}
