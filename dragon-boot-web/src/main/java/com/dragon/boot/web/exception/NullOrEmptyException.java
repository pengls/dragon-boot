package com.dragon.boot.web.exception;

/**
 * @ClassName NullOrEmptyException
 * @Author pengl
 * @Date 2019-05-30 10:10
 * @Description 缺少必要参数
 * @Version 1.0
 */
public class NullOrEmptyException extends RuntimeException {
    protected String message;

    public NullOrEmptyException() {
        setMessage("Required parameter missing!");
    }

    public NullOrEmptyException(String message) {
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
