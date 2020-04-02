package com.dragon.boot.ds.config;


import cn.hutool.core.date.DatePattern;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 自定义 p6spy sql输出格式
 *
 * @author zhanjin
 */
public class P6spySqlFormatConfigure implements MessageFormattingStrategy {

    /**
     * 过滤掉定时任务的 SQL
     */
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return StringUtils.isNotBlank(sql) ?  DatePattern.NORM_DATETIME_FORMAT.format(new Date())
                + " | 【SQL语句耗时】>>>> " + elapsed + " ms | 【SQL语句】>>>> ：" + StringUtils.LF + sql.replaceAll("[\\s]+", StringUtils.SPACE) + ";" : StringUtils.EMPTY;
    }
}
