package com.xuecheng.media.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.model.PageParam;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.model.dto.QueryMediaParamDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFilesService;
import org.springframework.stereotype.Service;

@Service
public class MediaFilesServiceImpl extends ServiceImpl<MediaFilesMapper, MediaFiles> implements MediaFilesService {
    @Override
    public Page<MediaFiles> listPage(PageParam pageParam, QueryMediaParamDto queryMediaParamDto) {
        Page<MediaFiles> mediaFilesPage = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        String fileType = queryMediaParamDto.getFileType();
        String filename = queryMediaParamDto.getFilename();
        String auditStatus = queryMediaParamDto.getAuditStatus();

        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(fileType), MediaFiles::getFileType, fileType)
                .like(StrUtil.isNotBlank(filename), MediaFiles::getFilename, filename)
                .eq(StrUtil.isNotBlank(auditStatus), MediaFiles::getAuditStatus, auditStatus);

        page(mediaFilesPage, queryWrapper);

        return mediaFilesPage;
    }
}
