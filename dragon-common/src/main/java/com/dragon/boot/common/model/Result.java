package com.dragon.boot.common.model;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName Result
 * @Author pengl
 * @Date 2019-05-14 16:32
 * @Description TODO
 * @Version 1.0
 */
@Setter
@Getter
public class Result<T> {
    private static final int SUC_CODE = 0;
    private static final String SUC_MSG = "成功";
    private boolean flag;
    private int resCode;
    private String resMsg;
    private T data;

    public Result() {
        this.flag = true;
        this.resCode = SUC_CODE;
        this.resMsg = SUC_MSG;
    }

    public Result(T data) {
        this.flag = true;
        this.resCode = SUC_CODE;
        this.resMsg = SUC_MSG;
        this.data = data;
    }

    public Result(boolean flag, int resCode, String resMsg, T data) {
        this.flag = flag;
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.data = data;
    }

    public Result(boolean flag, int resCode, String resMsg) {
        this.flag = flag;
        this.resCode = resCode;
        this.resMsg = resMsg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "flag=" + flag +
                ", resCode='" + resCode + '\'' +
                ", resMsg='" + resMsg + '\'' +
                ", data=" + JSON.toJSONString(data) +
                '}';
    }
}
