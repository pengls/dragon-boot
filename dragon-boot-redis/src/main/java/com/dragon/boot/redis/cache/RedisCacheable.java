package com.dragon.boot.redis.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author： pengl
 * @Date： 2017/11/10 22:52
 * @Description： 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RedisCacheable {
	//缓存key
	String cacheKey();
	//有效期时间（单位：秒）,默认2个小时
	int expire() default 7200;
	//缓存主动刷新时间（单位：秒），默认不主动刷新
	int reflash() default -1;
}
