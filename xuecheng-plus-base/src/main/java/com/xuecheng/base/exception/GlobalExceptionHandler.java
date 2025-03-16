package com.xuecheng.base.exception;

import com.xuecheng.base.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.warn(e.getMessage());
        return Result.fail(Error.UNKNOWN_ERROR);
    }

    @ExceptionHandler(CustomException.class)
    public Result<?> handleCustomException(CustomException ce) {
        log.warn(ce.getMessage());
        return Result.fail(ce.getMessage());
    }
}
