package com.dragon.boot.log.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dragon.boot.log.model.BusLog;

/**
 * @ClassName LogMapper
 * @Author pengl
 * @Date 2019-05-17 13:55
 * @Description 日志入库Mapper
 * @Version 1.0
 */
@DS("mslog")
public interface LogMapper extends BaseMapper<BusLog> {
}
