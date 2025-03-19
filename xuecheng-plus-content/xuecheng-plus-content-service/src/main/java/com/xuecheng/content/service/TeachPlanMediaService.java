package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.BindTeachPlanMediaDto;
import com.xuecheng.content.model.po.TeachPlanMedia;

public interface TeachPlanMediaService extends IService<TeachPlanMedia> {
    void bind(BindTeachPlanMediaDto bindTeachPlanMediaDto);
}
