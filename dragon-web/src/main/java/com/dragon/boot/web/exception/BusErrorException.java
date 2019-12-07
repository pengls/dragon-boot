package com.dragon.boot.web.exception;

/**
 * @ClassName BusErrorException
 * @Author pengl
 * @Date 2019-05-30 10:12
 * @Description 业务异常
 * @Version 1.0
 */
public class BusErrorException extends RuntimeException{
    protected String message;

    public BusErrorException() {
        setMessage("Program Throw Exception!");
    }

    public BusErrorException(String message) {
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
