package com.dragon.boot.log.constant;

/**
 * @ClassName LogConstant
 * @Author pengl
 * @Date 2019-11-15 15:51
 * @Description 常量类
 * @Version 1.0
 */
public class LogConstant {
    /**
     * sso类型
     */
    public enum SsoType {
        PRI("1");

        private String code;

        private SsoType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    /**
     * 日志级别
     */
    public enum LogLevel {
        INFO,
        SUCCESS,
        FAILD,
        ERROR
    }
}
