package com.dragon.boot.log.service;

import com.dragon.boot.log.model.BusLog;

/**
 * @ClassName LogService
 * @Author pengl
 * @Date 2019-05-15 20:08
 * @Description 日志操作业务层
 * @Version 1.0
 */
public interface LogService {
    /**
     * 插入elasticsearch
     *
     * @param logEntity
     */
    void insertES(BusLog logEntity);

    /**
     * 插入db
     *
     * @param logEntity
     */
    void insertDB(BusLog logEntity);

}
