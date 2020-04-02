package com.dragon.boot.log.config;

import com.dragon.boot.log.aop.LogAspect;
import com.dragon.boot.log.service.impl.LogServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName LogAutoConfig
 * @Author pengl
 * @Date 2019-05-15 18:20
 * @Description 自动配置类
 * @Version 1.0
 */
@Configuration
@EnableAsync
@ConditionalOnProperty(value = "dragon.boot.log.enable", havingValue = "true")
@Import({LogAspect.class, LogServiceImpl.class})
@MapperScan("com.dragon.boot.log.mapper")
public class LogAutoConfig {

    @Value("${dragon.boot.log.thread.corePoolSize:2}")
    private int corePoolSize;
    @Value("${dragon.boot.log.thread.maxPoolSize:5}")
    private int maxPoolSize;
    @Value("${dragon.boot.log.thread.queueCapacity:200}")
    private int queueCapacity;
    @Value("${dragon.boot.log.thread.keepAliveSeconds:180}")
    private int keepAliveSeconds;


    /**
     * @MethodName: asyncExecutor
     * @Author: pengl
     * @Date: 2019-05-15 19:56
     * @Description: 异步任务线程池
     * @Version: 1.0
     * @Param:
     * @Return:
     **/
    @Bean(name = "logAsyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        taskExecutor.setThreadNamePrefix("LogAsyncExecutor-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }
}
