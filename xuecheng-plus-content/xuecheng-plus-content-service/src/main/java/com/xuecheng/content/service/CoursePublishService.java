package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.po.CoursePublish;

public interface CoursePublishService extends IService<CoursePublish> {
    CoursePreviewDto preview(Long courseId);

    void commitAudit(Long courseId);
}
