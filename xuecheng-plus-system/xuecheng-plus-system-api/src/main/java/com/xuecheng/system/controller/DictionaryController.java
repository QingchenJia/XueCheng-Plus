package com.xuecheng.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.model.Result;
import com.xuecheng.system.model.po.Dictionary;
import com.xuecheng.system.service.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "字典管理接口")
@RestController
@RequestMapping("/system")
public class DictionaryController {
    @Resource
    private DictionaryService dictionaryService;

    @Operation(summary = "查询所有字典")
    @GetMapping("/dictionary/all")
    public Result<List<Dictionary>> queryAll() {
        List<Dictionary> dictionaries = dictionaryService.list();
        return Result.success(dictionaries);
    }

    @Operation(summary = "根据字典code查询字典")
    @GetMapping("/dictionary/code/{code}")
    public Result<Dictionary> getByCode(@PathVariable String code) {
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dictionary::getCode, code);
        Dictionary dictionary = dictionaryService.getOne(queryWrapper);

        return Result.success(dictionary);
    }
}
