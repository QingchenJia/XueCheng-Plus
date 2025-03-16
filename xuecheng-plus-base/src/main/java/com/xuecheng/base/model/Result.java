package com.xuecheng.base.model;

import com.xuecheng.base.exception.Error;
import lombok.Data;

@Data
public class Result<T> {
    private int code;

    private String message;

    private T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(1, "success", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(1, "success", data);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(0, message, null);
    }

    public static <T> Result<T> fail(Error error) {
        return new Result<>(0, error.getMessage(), null);
    }
}
