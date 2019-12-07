package com.dragon.boot.redis.cache;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * @author： pengl
 * @Date： 2017/11/13 10:33
 * @Description： 自定义缓存注解AOP实现
 */
@Slf4j
@Aspect
@Component
public class RedisCacheableAspect {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DefaultKeyGenerator defaultKeyGenerator;

    /**
     * @Description： 读取缓存数据
     * @param：
     * @return：
     * @throws：
     * @author： pengl
     * @Date： 2017/11/13 16:46
     */
    @Around(value = "@annotation(cache)")
    public Object cached(final ProceedingJoinPoint pjp, RedisCacheable cache) throws Throwable {
        try {
            //生成缓存KEY
            String key = defaultKeyGenerator.generateKey(pjp, cache.cacheKey());
            //获取缓存中的值
            ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
            Object value = valueOper.get(key);
            if (value != null) {
                //如果缓存有值，需要判断刷新缓存设置和当前缓存的失效时间
                int reflash = cache.reflash();
                if (reflash > 0) {
                    //查询当前缓存失效时间是否在主动刷新规则范围内
                    long exp = redisTemplate.getExpire(key, TimeUnit.SECONDS);
                    if (exp <= reflash) {
                        //主动刷新缓存，为不影响本次获取效率，采用异步线程刷新缓存
                        //TODO
                    }
                }
                return value;
            }
            value = pjp.proceed();
            //写入缓存
            if (cache.expire() > 0) {
                valueOper.set(key, value, cache.expire(), TimeUnit.SECONDS);  //否则设置缓存时间  ,序列化存储
            } else {
                valueOper.set(key, value);
            }
            return value;
        } catch (Exception e) {
            log.error("读取Redis缓存失败，异常信息：{}", e.getMessage());
            return pjp.proceed();
        }
    }

    /**
     * @Description： 新增缓存
     * @param：
     * @return：
     * @throws：
     * @author：pengl
     * @Date：2017/11/13 17:09
     */
    @Around(value = "@annotation(cacheput)")
    public Object cachePut(final ProceedingJoinPoint pjp, RedisCachePut cacheput) throws Throwable {
        try {
            String key = defaultKeyGenerator.generateKey(pjp, cacheput.cacheKey());
            Object valueData = pjp.proceed();
            ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
            if (cacheput.expire() > 0) {
                valueOper.set(key, pjp.getArgs()[0], cacheput.expire(), TimeUnit.SECONDS);
            } else {
                valueOper.set(key, pjp.getArgs()[0]);
            }
            return valueData;
        } catch (Exception e) {
            log.error("写入Redis缓存失败，异常信息：{}" , e.getMessage());
            return pjp.proceed();
        }
    }

    /**
     * @Description： 删除缓存
     * @param：
     * @return：
     * @throws：
     * @author： pengl
     * @Date：2017/11/13 17:09
     */
    @Around(value = "@annotation(cachevict)")
    public Object cacheEvict(final ProceedingJoinPoint pjp, RedisCacheEvict cachevict) throws Throwable {
        try {
            String key = defaultKeyGenerator.generateKey(pjp, cachevict.cacheKey());
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("删除Redis缓存失败，异常信息：{}" , e.getMessage());
        }
        return pjp.proceed();
    }

}
