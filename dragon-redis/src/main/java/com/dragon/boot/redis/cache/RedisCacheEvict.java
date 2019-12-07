package com.dragon.boot.redis.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author： pengl
 * @Date： 2017/11/10 16:40
 * @Description： 自定义Redis缓存清除注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RedisCacheEvict {
    String cacheKey();
}
