package com.xuecheng.media.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParam;
import com.xuecheng.base.model.Result;
import com.xuecheng.media.model.dto.QueryMediaParamDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFilesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "媒体资源管理服务")
@RestController
@RequestMapping("/media")
public class MediaFilesController {
    @Resource
    private MediaFilesService mediaFilesService;

    @Operation(summary = "媒体资源列表查询接口")
    @PostMapping("/files")
    public Result<Page<MediaFiles>> list(PageParam pageParam, @RequestBody(required = false) QueryMediaParamDto queryMediaParamDto) {
        Page<MediaFiles> mediaFilesPage = mediaFilesService.listPage(pageParam, queryMediaParamDto);
        return Result.success(mediaFilesPage);
    }
}
