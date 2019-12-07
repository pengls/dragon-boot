package com.dragon.boot.log.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName Log
 * @Author pengl
 * @Date 2019-05-15 14:53
 * @Description 日志注解
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Log {
    /**
     * 模块名称
     * @return
     */
    String model() default "";
    /**
     * 操作描述
     */
    String desc() default "";

    /**
     * 是否保存到数据库
     * @return
     */
    boolean saveDB() default true;

    /**
     * 是否推送到ES服务器
     * @return
     */
    boolean saveES() default  false;
}
