package com.xuecheng.base.exception;

import lombok.Getter;

@Getter
public enum Error {
    UNKNOWN_ERROR("执行过程异常，请重试"),
    PARAMS_ERROR("非法参数"),
    OBJECT_NULL("对象为空"),
    QUERY_NULL("查询结果为空"),
    REQUEST_NULL("请求参数为空");

    private final String message;

    Error(String message) {
        this.message = message;
    }
}
