package com.xuecheng.content.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.po.CourseCategory;

import java.util.List;

public interface CourseCategoryService extends IService<CourseCategory> {
    List<Tree<String>> queryTreeNodes();
}
