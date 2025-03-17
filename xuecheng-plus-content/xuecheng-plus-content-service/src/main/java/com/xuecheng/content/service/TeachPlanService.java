package com.xuecheng.content.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.po.TeachPlan;

import java.util.List;

public interface TeachPlanService extends IService<TeachPlan> {
    List<Tree<String>> getPlanTree(Long courseId);
}
