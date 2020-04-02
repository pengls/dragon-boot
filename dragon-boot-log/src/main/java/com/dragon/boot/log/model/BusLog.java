package com.dragon.boot.log.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName BusLog
 * @Author pengl
 * @Date 2019-05-15 14:31
 * @Description 通用业务日志实体类
 */
@TableName("tb_bus_log")
@Data
@Builder
public class BusLog implements Serializable {
    @Tolerate
    public BusLog(){}

    /**
     * 唯一主键
     */
    @TableId("log_id")
    private String logId;
    /**
     * 所属应用
     */
    private String app;
    /**
     * 所属模块
     */
    private String model;
    /**
     * 操作描述
     */
    @TableField("bus_desc")
    private String busDesc;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 节点信息
     */
    private String node;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 自定义数据
     */
    private String dataset;
    /**
     * 操作结果
     */
    private String result;
    /**
     * 操作耗时
     */
    private Long times;
    /**
     * 状态 -2 逻辑删除
     */
    private int status;
    /**
     * 完成时间
     */
    @TableField("finish_time")
    private Date finishTime;
}
