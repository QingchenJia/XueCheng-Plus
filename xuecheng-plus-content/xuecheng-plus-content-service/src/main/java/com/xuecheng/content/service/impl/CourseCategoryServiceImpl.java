package com.xuecheng.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryDto;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.service.CourseCategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {
    /**
     * 查询课程类别树节点
     * <p>
     * 本方法用于构建课程类别的树形结构，首先获取所有课程类别，然后将其转换为DTO列表，
     * 并移除第一个类别（可能是因为第一个类别不符合展示要求或有特殊用途），
     * 最后调用buildTree方法构建树形结构
     *
     * @return 返回构建好的课程类别树形结构列表
     */
    @Override
    public List<CourseCategoryDto> queryTreeNodes() {
        // 获取所有课程类别列表
        List<CourseCategory> courseCategories = list();
        // 移除列表中的第一个类别
        courseCategories.removeFirst();

        // 将课程类别列表转换为CourseCategoryDto列表，便于后续操作和返回
        List<CourseCategoryDto> courseCategoryDtos = courseCategories.stream()
                .map(courseCategory -> BeanUtil.copyProperties(courseCategory, CourseCategoryDto.class))
                .toList();
        // 初始化一个空的列表，用于存放最终构建的树形结构
        List<CourseCategoryDto> tree = new ArrayList<>();

        // 调用buildTree方法，构建树形结构并返回
        return buildTree(tree, courseCategoryDtos);
    }

    /**
     * 在课程类别列表中查找指定类别的子类别
     *
     * @param courseCategoryDto  指定的课程类别DTO，用于查找其子类别
     * @param courseCategoryDtos 包含所有课程类别的列表，从中查找子类别
     * @return 返回一个列表，包含所有找到的子类别如果指定类别没有子类别，则返回空列表
     */
    private List<CourseCategoryDto> findChildren(CourseCategoryDto courseCategoryDto, List<CourseCategoryDto> courseCategoryDtos) {
        // 使用流处理从列表中过滤出父类别ID与指定类别ID匹配的子类别，并按顺序字段排序
        List<CourseCategoryDto> children = courseCategoryDtos.stream()
                .filter(item -> item.getParentId().equals(courseCategoryDto.getId()))
                .sorted(Comparator.comparing(CourseCategory::getOrderBy))
                .toList();

        // 如果没有找到子类别，返回空列表，否则返回找到的子类别列表
        return CollUtil.isEmpty(children) ? Collections.emptyList() : children;
    }

    /**
     * 构建课程类别树结构
     *
     * @param tree               用于构建树结构的初始列表，如果为空，则从根节点开始构建
     * @param courseCategoryDtos 包含所有课程类别的列表，用于构建树结构
     * @return 返回构建完成的课程类别树结构列表
     */
    private List<CourseCategoryDto> buildTree(List<CourseCategoryDto> tree, List<CourseCategoryDto> courseCategoryDtos) {
        // 如果初始列表为空，则从所有根节点（父ID为"1"）开始构建树结构
        if (CollUtil.isEmpty(tree)) {
            tree = courseCategoryDtos.stream()
                    .filter(item -> item.getParentId().equals("1"))
                    .sorted(Comparator.comparing(CourseCategory::getOrderBy))
                    .toList();
        }

        // 遍历当前树结构的每个节点，为它们找到并设置子节点
        for (CourseCategoryDto node : tree) {
            // 查找当前节点的子节点
            List<CourseCategoryDto> children = findChildren(node, courseCategoryDtos);

            // 如果当前节点有子节点，则设置子节点并递归构建子节点的树结构
            if (CollUtil.isNotEmpty(children)) {
                node.setChildren(children);
                buildTree(children, courseCategoryDtos);
            }
        }

        // 返回构建完成的树结构列表
        return tree;
    }
}
