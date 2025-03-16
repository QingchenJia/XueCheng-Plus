package com.xuecheng.content.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.service.CourseCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {
    /**
     * 查询并构建树形结构的课程类别节点
     * <p>
     * 此方法从课程类别列表中构建一个树形结构，用于表示课程类别的层级关系
     * 它首先获取一个课程类别列表，然后移除列表中的第一个元素，以特定的规则构建树形结构
     *
     * @return 返回一个树形结构的列表，其中每个节点都是一个课程类别
     */
    @Override
    public List<Tree<String>> queryTreeNodes() {
        // 获取课程类别列表
        List<CourseCategory> courseCategories = list();
        // 移除列表中的第一个元素，以便从第二个类别开始构建树形结构
        courseCategories.removeFirst();

        // 创建并配置树节点配置对象
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setWeightKey("orderBy");

        // 使用配置信息和课程类别列表构建树形结构
        return TreeUtil.build(
                courseCategories,
                "1",
                treeNodeConfig,
                (courseCategory, treeNode) -> {
                    // 将课程类别的属性值赋给树节点
                    treeNode.setId(courseCategory.getId());
                    treeNode.setParentId(courseCategory.getParentId());
                    treeNode.setName(courseCategory.getName());
                    treeNode.setWeight(courseCategory.getOrderBy());
                    // 添加额外的属性信息
                    treeNode.putExtra("label", courseCategory.getLabel());
                    treeNode.putExtra("isShow", courseCategory.getIsShow());
                    treeNode.putExtra("isLeaf", courseCategory.getIsLeaf());
                }
        );
    }
}
