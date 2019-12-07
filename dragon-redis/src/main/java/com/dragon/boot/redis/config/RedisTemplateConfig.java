package com.dragon.boot.redis.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.dragon.boot.redis.cache.DefaultKeyGenerator;
import com.dragon.boot.redis.cache.RedisCacheableAspect;
import com.dragon.boot.redis.utils.RedisDistributedLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @ClassName RedisTemplateConfig
 * @Author pengl
 * @Date 2019-04-23 18:39
 * @Description 使用SpringBoot-Auto-Config自动读取配置，自定义RedisTemplate以满足key前缀需求
 * @Version 1.0
 */
/**
 * # redis配置
 * spring.redis.cluster.nodes=
 * spring.redis.host=
 * spring.redis.port=
 * spring.redis.password=
 * spring.redis.lettuce.pool.min-idle=1
 * spring.redis.lettuce.pool.max-idle=150
 * spring.redis.lettuce.pool.max-active=512
 * spring.redis.lettuce.pool.max-wait=2000
 * spring.redis.lettuce.pool.timeout=3000
 */
@Configuration
@AutoConfigureBefore(value = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
@Import({RedisDistributedLock.class, DefaultKeyGenerator.class, RedisCacheableAspect.class})
@ConditionalOnProperty(name = "dragon.boot.redis.enable", havingValue = "true", matchIfMissing = true)
public class RedisTemplateConfig {
    @Value("${spring.application.name}")
    private String pre;
    @Value("${dragon.boot.redis.pre.enable:false}")
    private boolean enablePre;

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate template = new RedisTemplate();
        RedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        StrRedisSerializer strRedisSerializer = enablePre ? new StrRedisSerializer(pre) : new StrRedisSerializer();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(strRedisSerializer);
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashKeySerializer(strRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        return template;
    }

}
