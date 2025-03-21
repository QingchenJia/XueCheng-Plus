package com.xuecheng.content;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.model.dto.TeachPlanDto;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.TeachPlan;
import com.xuecheng.content.model.po.TeachPlanMedia;
import com.xuecheng.content.service.CourseCategoryService;
import com.xuecheng.content.service.TeachPlanMediaService;
import com.xuecheng.content.service.TeachPlanService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ContentApplicationTest {
    @Resource
    private CourseCategoryService courseCategoryService;

    @Resource
    private TeachPlanService teachPlanService;

    @Resource
    private TeachPlanMediaService teachPlanMediaService;

    @Test
    void buildCourseCategoryTree() {
        List<CourseCategory> courseCategories = courseCategoryService.list();
        courseCategories.removeFirst();

        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("orderBy");

        List<Tree<String>> courseCategoryTree = TreeUtil.build(
                courseCategories,
                "1",
                treeNodeConfig,
                (courseCategory, treeNode) -> {
                    treeNode.setId(courseCategory.getId());
                    treeNode.setParentId(courseCategory.getParentId());
                    treeNode.setName(courseCategory.getName());
                    treeNode.setWeight(courseCategory.getOrderBy());
                    treeNode.putExtra("label", courseCategory.getLabel());
                    treeNode.putExtra("isShow", courseCategory.getIsShow());
                    treeNode.putExtra("isLeaf", courseCategory.getIsLeaf());
                }
        );

        System.out.println(JSONUtil.toJsonStr(courseCategoryTree));
    }

    @Test
    void buildTeachPlanTree() {
        int courseId = 121;
        LambdaQueryWrapper<TeachPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachPlan::getCourseId, courseId);
        List<TeachPlan> teachPlans = teachPlanService.list(queryWrapper);

        List<TeachPlanDto> teachPlanDtos = teachPlans.stream()
                .map(teachPlan -> {
                    LambdaQueryWrapper<TeachPlanMedia> teachPlanMediaQueryWrapper = new LambdaQueryWrapper<>();
                    teachPlanMediaQueryWrapper.eq(TeachPlanMedia::getTeachPlanId, teachPlan.getId());
                    TeachPlanMedia teachPlanMedia = teachPlanMediaService.getOne(teachPlanMediaQueryWrapper);

                    TeachPlanDto teachPlanDto = BeanUtil.copyProperties(teachPlan, TeachPlanDto.class);
                    teachPlanDto.setTeachPlanMedia(teachPlanMedia);

                    return teachPlanDto;
                })
                .toList();

        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("orderBy");

        List<Tree<String>> teachPlanTree = TreeUtil.build(
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

        System.out.println(JSONUtil.toJsonStr(teachPlanTree));
    }
}
