package com.xuecheng.content.controller;

import cn.hutool.core.lang.tree.Tree;
import com.xuecheng.base.model.Result;
import com.xuecheng.content.model.dto.SaveOrUpdateTeachPlanDto;
import com.xuecheng.content.service.TeachPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "课程计划创建或修改")
    @PostMapping("/teachPlan")
    public Result<?> saveOrUpdateTeachPlan(@RequestBody SaveOrUpdateTeachPlanDto saveOrUpdateTeachPlanDto) {
        teachPlanService.saveOrUpdateInfo(saveOrUpdateTeachPlanDto);
        return Result.success();
    }

    @Operation(summary = "课程计划删除")
    @DeleteMapping("/teachPlan/{id}")
    public Result<?> deletePlan(@PathVariable Long id) {
        teachPlanService.deleteInfo(id);
        return Result.success();
    }

    @Operation(summary = "课程计划上移")
    @PostMapping("/teachPlan/moveUp/{id}")
    public Result<?> moveUp(@PathVariable Long id) {
        teachPlanService.moveUp(id);
        return Result.success();
    }

    @Operation(summary = "课程计划下移")
    @PostMapping("/teachPlan/moveDown/{id}")
    public Result<?> moveDown(@PathVariable Long id) {
        teachPlanService.moveDown(id);
        return Result.success();
    }
}
