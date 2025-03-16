package com.xuecheng.base.exception;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }

    public CustomException(Error error) {
        super(error.getMessage());
    }
}
