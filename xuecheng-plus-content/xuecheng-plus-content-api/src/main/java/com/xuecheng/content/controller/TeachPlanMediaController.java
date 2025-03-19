package com.xuecheng.content.controller;

import com.xuecheng.base.model.Result;
import com.xuecheng.content.model.dto.BindTeachPlanMediaDto;
import com.xuecheng.content.service.TeachPlanMediaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content")
public class TeachPlanMediaController {
    @Resource
    private TeachPlanMediaService teachPlanMediaService;

    @Operation(summary = "课程计划和媒资信息绑定")
    @PostMapping("/teachPlan/association/media")
    public Result<?> associationMedia(@RequestBody BindTeachPlanMediaDto bindTeachPlanMediaDto) {
        teachPlanMediaService.bind(bindTeachPlanMediaDto);
        return Result.success();
    }
}
