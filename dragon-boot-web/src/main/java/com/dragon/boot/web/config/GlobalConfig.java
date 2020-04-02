package com.dragon.boot.web.config;

import com.dragon.boot.web.xss.XssFilterBaseWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.servlet.Filter;
import java.util.Arrays;

/**
 * @ClassName GlobalConfig
 * @Author pengl
 * @Date 2019-10-09 17:11
 * @Description 全局配置
 * @Version 1.0
 */
@Configuration
public class GlobalConfig implements WebMvcConfigurer {

    /**
     * 忽略属性配置
     *
     * @return
     */
    @ConditionalOnProperty(name = "dragon.boot.ignoreUnresolvablePlaceholders", havingValue = "true")
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreUnresolvablePlaceholders(true);
        return configurer;
    }

    /**
     * 跨域配置
     *
     * @return
     */
    @ConditionalOnProperty(name = "dragon.boot.cros.enable", havingValue = "true")
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "HEAD"));
        /**是否允许发送cookie**/
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(corsConfiguration.getMaxAge());

        source.registerCorsConfiguration("/**", corsConfiguration);

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    /**
     * xss过滤配置
     *
     * @return
     */
    @ConditionalOnProperty(name = "dragon.boot.xss.enable", havingValue = "true")
    @Bean
    public FilterRegistrationBean xssFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XssFilterBaseWrapper());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE - 1);
        return registrationBean;
    }

}
