package com.dragon.boot.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ErrorProperties
 * @Author pengl
 * @Date 2019-05-29 13:57
 * @Description TODO
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "error")
public class ErrorProperties {
    public Map<Integer, String> convert = new HashMap<>();

    public Map<Integer, String> getConvert() {
        return convert;
    }

    public void setConvert(Map<Integer, String> convert) {
        this.convert = convert;
    }
}
