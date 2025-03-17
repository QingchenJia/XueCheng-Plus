package com.xuecheng.content.controller;

import cn.hutool.core.lang.tree.Tree;
import com.xuecheng.base.model.Result;
import com.xuecheng.content.service.TeachPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "课程计划管理接口")
@RestController
@RequestMapping("/content")
public class TeachPlanController {
    @Resource
    private TeachPlanService teachPlanService;

    @Operation(summary = "查询课程计划树形结构")
    @GetMapping("/teachPlan/{courseId}/tree-nodes")
    public Result<List<Tree<String>>> getTreeNodes(@PathVariable Long courseId) {
        List<Tree<String>> coursePlanTree = teachPlanService.getPlanTree(courseId);
        return Result.success(coursePlanTree);
    }
}
