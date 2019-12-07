package com.dragon.boot.web.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName DateConverterConfig
 * @Author pengl
 * @Date 2019-05-28 15:59
 * @Description 错误信息客户化转换器
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(ErrorProperties.class)
public class ErrorConverterConfig{
    
}
