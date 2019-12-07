package com.dragon.boot.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisDistributedLock
 * @Author pengl
 * @Date 2019-04-23 18:59
 * @Description redis分布式锁
 * @Version 1.0
 */
public class RedisDistributedLock {

    //private static final Logger LOG = LoggerFactory.getLogger(RedisDistributedLock.class);

    private static final String LOCK_PREIX = "redis_lock:";

    //private static final String UNLOCK_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    //private static final String LOCK_LUA = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end";

    //private static final Long SUCCESS = 1L;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @MethodName: lock
     * @Author: pengl
     * @Date: 2019-04-23 19:15
     * @Description: 设置锁: 如果返回null，代表获取失败
     * @Version: 1.0
     * @Param: key:锁的key指； expire:失效时间，单位秒
     * @Return:
     **/
    public boolean lock(String key, int expire) {
        return redisTemplate.opsForValue().setIfAbsent(LOCK_PREIX + key, UUID.randomUUID().toString(), expire, TimeUnit.SECONDS);
    }

    /**
     * @MethodName: unlock
     * @Author: pengl
     * @Date: 2019-04-23 19:17
     * @Description: 释放锁
     * @Version: 1.0
     * @Param:
     * @Return:
     **/
    public boolean unlock(String key) {
        return redisTemplate.delete(LOCK_PREIX + key);
    }

}
