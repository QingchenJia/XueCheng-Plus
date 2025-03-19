package com.xuecheng.media.controller;

import com.xuecheng.base.model.Result;
import com.xuecheng.media.service.MediaFilesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "媒体资源访问接口")
@RestController
@RequestMapping("/media")
public class OpenMediaController {
    @Resource
    private MediaFilesService mediaFilesService;

    @Operation(summary = "预览文件")
    @GetMapping("/open/preview/{mediaId}")
    public Result<String> getPlayUrlByMediaId(@PathVariable String mediaId) {
        String url = mediaFilesService.getUrl(mediaId);
        return Result.success(url);
    }
}
