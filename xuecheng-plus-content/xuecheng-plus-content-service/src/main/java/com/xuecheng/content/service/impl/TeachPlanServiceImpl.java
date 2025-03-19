package com.xuecheng.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.CustomException;
import com.xuecheng.content.mapper.TeachPlanMapper;
import com.xuecheng.content.model.dto.SaveOrUpdateTeachPlanDto;
import com.xuecheng.content.model.dto.TeachPlanDto;
import com.xuecheng.content.model.po.TeachPlan;
import com.xuecheng.content.model.po.TeachPlanMedia;
import com.xuecheng.content.service.TeachPlanMediaService;
import com.xuecheng.content.service.TeachPlanService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TeachPlanServiceImpl extends ServiceImpl<TeachPlanMapper, TeachPlan> implements TeachPlanService {
    @Resource
    @Lazy
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
                    treeNode.putExtra("createDate", teachPlanDto.getCreateDate() == null ? null : teachPlanDto.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    treeNode.putExtra("changeDate", teachPlanDto.getChangeDate() == null ? null : teachPlanDto.getChangeDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    treeNode.putExtra("teachPlanMedia", teachPlanDto.getTeachPlanMedia());
                }
        );
    }

    /**
     * 保存或更新教学计划信息
     * <p>
     * 此方法根据提供的DTO对象中的信息，决定是保存新教学计划还是更新现有教学计划
     * 如果DTO中的ID为空，则视为新建并保存；如果ID存在，则视为更新操作
     *
     * @param saveOrUpdateTeachPlanDto 包含要保存或更新的教学计划信息的DTO对象
     */
    @Override
    public void saveOrUpdateInfo(SaveOrUpdateTeachPlanDto saveOrUpdateTeachPlanDto) {
        // 获取DTO中的ID
        Long id = saveOrUpdateTeachPlanDto.getId();

        // 将DTO转换为TeachPlan实体类对象
        TeachPlan teachPlan = BeanUtil.copyProperties(saveOrUpdateTeachPlanDto, TeachPlan.class);

        // 判断是否为新建教学计划
        if (id == null) {
            // 创建查询条件，用于检查是否有相同课程ID和父ID的教学计划已存在
            LambdaQueryWrapper<TeachPlan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TeachPlan::getCourseId, teachPlan.getCourseId())
                    .eq(TeachPlan::getParentId, teachPlan.getParentId());

            // 获取满足条件的教学计划数量
            int count = (int) count(queryWrapper);

            // 设置新教学计划的排序字段值，使其在同级节点中排序
            teachPlan.setOrderBy(++count);

            // 保存新教学计划
            save(teachPlan);
        } else {
            // 更新现有教学计划
            updateById(teachPlan);
        }
    }

    /**
     * 删除课程计划信息
     * <p>
     * 此方法首先检查是否有子级课程计划信息存在，如果存在，则抛出异常，表示不能删除
     * 如果不存在子级信息，则删除指定的课程计划信息及其关联的媒体信息
     *
     * @param id 课程计划信息的ID
     * @throws CustomException 如果课程计划信息还有子级信息时抛出此异常
     */
    @Override
    public void deleteInfo(Long id) {
        // 创建查询包装器以查询具有相同父ID的课程计划信息
        LambdaQueryWrapper<TeachPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachPlan::getParentId, id);
        // 计算具有相同父ID的课程计划信息数量
        long count = count(queryWrapper);

        // 如果存在子级信息，则抛出异常，表示不能删除
        if (count > 0) {
            throw new CustomException("课程计划信息还有子级信息，无法操作");
        }

        // 删除课程计划信息
        removeById(id);

        // 创建查询包装器以查询与课程计划ID关联的媒体信息
        LambdaQueryWrapper<TeachPlanMedia> teachPlanMediaQueryWrapper = new LambdaQueryWrapper<>();
        teachPlanMediaQueryWrapper.eq(TeachPlanMedia::getTeachPlanId, id);

        // 删除与课程计划ID关联的媒体信息
        teachPlanMediaService.remove(teachPlanMediaQueryWrapper);
    }

    /**
     * 上移指定教学计划的顺序
     * 如果当前教学计划已经在最顶部，则抛出异常
     * 否则，找到前一个教学计划并与其交换顺序
     *
     * @param id 教学计划的ID
     * @throws CustomException 如果当前教学计划已经在最顶部时
     */
    @Override
    @Transactional
    public void moveUp(Long id) {
        // 根据ID获取教学计划
        TeachPlan teachPlan = getById(id);
        int orderBy = teachPlan.getOrderBy();

        // 如果当前教学计划已经在最顶部，抛出异常
        if (orderBy == 1) {
            throw new CustomException("当前章节在开头，无法上移");
        }

        // 创建查询条件，寻找同一父级且顺序值为当前顺序值减一的教学计划
        LambdaQueryWrapper<TeachPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachPlan::getParentId, teachPlan.getParentId())
                .eq(TeachPlan::getOrderBy, --orderBy);

        // 获取前一个教学计划
        TeachPlan onTeachPlan = getOne(queryWrapper);
        // 将前一个教学计划的顺序值增加1，即向下移动
        onTeachPlan.setOrderBy(onTeachPlan.getOrderBy() + 1);
        updateById(onTeachPlan);

        // 将当前教学计划的顺序值减一，即向上移动
        teachPlan.setOrderBy(orderBy);
        updateById(teachPlan);
    }

    /**
     * 下移指定教学计划的顺序
     * 如果当前教学计划已经在其父级中的最后位置，则抛出异常
     * 否则，与下一个教学计划交换顺序，以实现下移效果
     *
     * @param id 要下移的教学计划的ID
     * @throws CustomException 如果当前教学计划已经在末尾，无法下移
     */
    @Override
    @Transactional
    public void moveDown(Long id) {
        // 获取当前教学计划
        TeachPlan teachPlan = getById(id);
        int orderBy = teachPlan.getOrderBy();

        // 创建查询条件，查找与当前教学计划同一父级的所有记录
        LambdaQueryWrapper<TeachPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachPlan::getParentId, teachPlan.getParentId());
        // 计算同一父级下的教学计划总数
        int count = (int) count(queryWrapper);

        // 如果当前教学计划的顺序号等于总数，说明已经在末尾，无法下移
        if (orderBy == count) {
            throw new CustomException("当前章节在末尾，无法下移");
        }

        // 创建查询条件，查找下一个教学计划
        LambdaQueryWrapper<TeachPlan> teachPlanQueryWrapper = new LambdaQueryWrapper<>();
        teachPlanQueryWrapper.eq(TeachPlan::getParentId, teachPlan.getParentId())
                .eq(TeachPlan::getOrderBy, ++orderBy);
        // 获取下一个教学计划
        TeachPlan downTeachPlan = getOne(teachPlanQueryWrapper);

        // 交换下一个教学计划的顺序号，使其顺序号减1
        downTeachPlan.setOrderBy(downTeachPlan.getOrderBy() - 1);
        updateById(downTeachPlan);

        // 更新当前教学计划的顺序号，使其顺序号加1
        teachPlan.setOrderBy(orderBy);
        updateById(teachPlan);
    }
}
