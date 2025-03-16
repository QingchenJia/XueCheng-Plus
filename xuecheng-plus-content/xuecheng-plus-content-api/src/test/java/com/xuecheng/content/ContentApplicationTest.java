package com.xuecheng.content;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.json.JSONUtil;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.service.CourseCategoryService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ContentApplicationTest {
    @Resource
    private CourseCategoryService courseCategoryService;

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
}
