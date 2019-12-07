package com.dragon.boot.web.config;

import com.dragon.boot.common.model.Result;
import com.dragon.boot.web.exception.BadRequestException;
import com.dragon.boot.web.exception.BusErrorException;
import com.dragon.boot.web.exception.NullOrEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;

/**
 * @ClassName GlobalExceptionHandler
 * @Author pengl
 * @Date 2019-05-30 10:15
 * @Description 全局异常处理
 * @Version 1.0
 */
@ConditionalOnProperty(name = "dragon.boot.exception.enable", havingValue = "true")
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final int DEFAULT_ERROR_CODE = -1;

    @ExceptionHandler(NullOrEmptyException.class)
    @ResponseBody
    public Result nullOrEmptyExceptionHandler(HttpServletRequest request, NullOrEmptyException exception) {
        return handleErrorInfo(exception.getMessage());
    }

    @ExceptionHandler(BusErrorException.class)
    @ResponseBody
    public Result busErrorException(HttpServletRequest request, BusErrorException exception) {
        return handleErrorInfo(exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public Result busErrorException(HttpServletRequest request, BadRequestException exception) {
        return handleErrorInfo(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Result illegalPropExceptionHandler(HttpServletRequest request, IllegalArgumentException exception) {
        return handleErrorInfo(exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest request, IllegalArgumentException exception) {
        return handleErrorInfo("Request Method Not Supported!");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, Exception exception) {
        log.error("统一异常处理捕获异常：{}", exception.getMessage(), exception);
        return handleErrorInfo(exception.getMessage());
    }

    private Result handleErrorInfo(String message) {
        return new Result(false, DEFAULT_ERROR_CODE, message);
    }


    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e BindException
     * @return FebsResponse
     */
    @ExceptionHandler(BindException.class)
    public Result validExceptionHandler(BindException e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return handleErrorInfo(message.toString());
    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(pathArr[1]).append(violation.getMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return handleErrorInfo(message.toString());
    }
}
