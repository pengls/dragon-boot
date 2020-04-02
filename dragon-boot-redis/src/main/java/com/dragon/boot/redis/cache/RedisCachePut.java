package com.dragon.boot.redis.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author： pengl
 * @Date： 2017/11/10 16:41
 * @Description： 自定义新增Redis缓存注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RedisCachePut {
    //缓存key
    String cacheKey();
    //有效期时间（单位：秒）,默认2个小时
    int expire() default 7200;
}
