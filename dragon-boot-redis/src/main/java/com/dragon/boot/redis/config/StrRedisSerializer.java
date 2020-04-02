package com.dragon.boot.redis.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * @ClassName MyStringRedisSerializer
 * @Author pengl
 * @Date 2019/1/7 12:44
 * @Description 自定义redis key序列化 添加默认的KEY
 * @Version 1.0
 */
public class StrRedisSerializer implements RedisSerializer<String> {

    private final Charset charset = Charset.forName("utf-8");

    private String pre;

    public StrRedisSerializer(String pre) {
        this.pre = pre;
    }

    public StrRedisSerializer() {

    }

    @Override
    public byte[] serialize(String key) throws SerializationException {
        return (key == null ? null : StringUtils.isBlank(pre) ? key.getBytes(charset) : (pre + ":" + key).getBytes(charset));
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        String key = bytes == null ? null : new String(bytes, charset);
        if (key == null) {
            return null;
        }
        if (StringUtils.isNotBlank(pre)) {
            int indexOf = key.indexOf(pre);
            if (indexOf > 0) {
                key = key.substring(indexOf);
            }
        }
        return key;
    }
}
