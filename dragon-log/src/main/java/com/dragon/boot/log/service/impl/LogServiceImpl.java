package com.dragon.boot.log.service.impl;

import com.dragon.boot.log.mapper.LogMapper;
import com.dragon.boot.log.model.BusLog;
import com.dragon.boot.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

/**
 * @ClassName LogServiceImpl
 * @Author pengl
 * @Date 2019-11-15 15:43
 * @Description 日志操作业务实现
 * @Version 1.0
 */
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;

    @Async(value = "logAsyncExecutor")
    public void insertES(BusLog logEntity){
        //TODO
    }

    @Async(value = "logAsyncExecutor")
    public void insertDB(BusLog logEntity){
        logMapper.insert(logEntity);
    }
}
