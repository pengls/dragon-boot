package com.dragon.boot.web.exception;

/**
 * @ClassName BadRequestException
 * @Author pengl
 * @Date 2019-05-30 10:12
 * @Description 无效参数
 * @Version 1.0
 */
public class BadRequestException extends RuntimeException{
    protected String message;

    public BadRequestException() {
        setMessage("Bad Request!");
    }

    public BadRequestException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
