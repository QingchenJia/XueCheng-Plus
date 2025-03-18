package com.xuecheng.media.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.PageParam;
import com.xuecheng.media.model.dto.QueryMediaParamDto;
import com.xuecheng.media.model.po.MediaFiles;

public interface MediaFilesService extends IService<MediaFiles> {
    Page<MediaFiles> listPage(PageParam pageParam, QueryMediaParamDto queryMediaParamDto);
}
