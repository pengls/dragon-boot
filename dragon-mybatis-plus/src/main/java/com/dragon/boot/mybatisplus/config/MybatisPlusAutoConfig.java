package com.dragon.boot.mybatisplus.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.dragon.boot.mybatisplus.anno.MapperScanner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName MybatisPlusAutoConfig
 * @Author pengl
 * @Date 2019-05-10 11:11
 * @Description Mybatis-Plus配置类
 * @Version 1.0
 */
@EnableTransactionManagement
@Configuration
@MapperScanner(basePackages = {"${drogan.boot.mybatis-plus.mapper-scan}"})
public class MybatisPlusAutoConfig {
    /**
     * 分页插件
     */
    @Bean
    @ConditionalOnProperty(name = "drogan.boot.mybatis-plus.page.enable", havingValue = "true", matchIfMissing = true)
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
