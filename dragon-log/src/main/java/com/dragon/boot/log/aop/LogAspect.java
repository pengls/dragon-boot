package com.dragon.boot.log.aop;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.dragon.boot.common.model.Result;
import com.dragon.boot.log.LogContext;
import com.dragon.boot.log.anno.Log;
import com.dragon.boot.log.constant.LogConstant;
import com.dragon.boot.log.model.BusLog;
import com.dragon.boot.log.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * @ClassName: LogAspect
 * @Author: pengl
 * @Date: 2019-05-15 14:57
 * @Description: 日志AOP拦截
 * @Version: 1.0
 **/
@Aspect
@Component
@Slf4j
public class LogAspect implements EnvironmentAware, ApplicationListener<WebServerInitializedEvent> {
    private String appName;
    private int serverPort;
    private String localIp;
    private String ssoType;

    @Autowired
    private LogService logService;


    /**
     * @MethodName: saveLog
     * @Author: pengl
     * @Date: 2019-05-15 15:56
     * @Description: 保存日志
     * @Version: 1.0
     * @Param: [pjp, log]
     * @Return: java.lang.Object
     **/
    @Around(value = "@annotation(logAnno)")
    public Object saveLog(final ProceedingJoinPoint pjp, Log logAnno) throws Throwable {
        Date bgTime = new Date();
        Object o = pjp.proceed();
        Date endTime = new Date();
        saveLog(pjp, o, logAnno, bgTime, endTime);
        return o;
    }

    /**
     * @MethodName: afterExcpt
     * @Author: pengl
     * @Date: 2019-11-18 13:57
     * @Description: 异常情况
     * @Version: 1.0
     * @Param: [pjp, logAnno, ex]
     * @Return: void
     **/
    @AfterThrowing(value = "@annotation(logAnno)", throwing = "ex")
    public void afterExcpt(final JoinPoint pjp, Log logAnno, Exception ex) {
        Date now = new Date();
        saveLog(pjp, ex, logAnno, now, now);
    }

    private void saveLog(final JoinPoint pjp, Object o, Log logAnno, Date bgTime, Date endTime) {
        try {
            BusLog logEntity = getLogEntity(pjp, logAnno, o, bgTime, endTime);
            log.info("[统一日志aop]收集业务日志：{}", logEntity);

            if (logAnno.saveES()) {
                logService.insertES(logEntity);
            }

            if (logAnno.saveDB()) {
                logService.insertDB(logEntity);
            }
        } catch (Exception e) {
            log.error("[统一日志aop]日志保存异常：{}", e.getMessage(), e);
        }
    }

    /**
     * @MethodName: getLogEntity
     * @Author: pengl
     * @Date: 2019-05-15 19:40
     * @Description: 获取日志数据
     * @Version: 1.0
     * @Param: [pjp, log, o, times]
     * @Return: com.dragon.boot.log.model.BusLog
     **/
    private BusLog getLogEntity(final JoinPoint pjp, Log logAnno, Object o, Date bgTime, Date endTime) {

        /**
         * 优先读取local线程中的日志数据
         */
        BusLog logEntity = LogContext.get();
        if (null == logEntity) {
            logEntity = new BusLog();
        }
        LogContext.clear();

        logEntity.setLogId(IdUtil.fastSimpleUUID());
        logEntity.setApp(appName);
        logEntity.setCreateTime(bgTime);
        if (null == logEntity.getModel()) {
            logEntity.setModel(logAnno.model());
        }
        if (null == logEntity.getBusDesc()) {
            logEntity.setBusDesc(logAnno.desc());
        }
        logEntity.setNode(getLocalIp() + ":" + serverPort);
        logEntity.setStatus(1);
        logEntity.setFinishTime(endTime);
        logEntity.setTimes(DateUtil.between(bgTime, endTime, DateUnit.MS));

        /**
         * 获取当前用户信息
         */
        if (null == logEntity.getOperator() && StringUtils.isNotBlank(ssoType)) {
            if (LogConstant.SsoType.PRI.getCode().equals(ssoType)) {
                logEntity.setOperator("xx");
            }
        }

        /**
         * 如果没有手动指定模块名称，取方法名称
         */
        if (StrUtil.isBlank(logAnno.model())) {
            Signature signature = pjp.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method currentMethod;
            try {
                currentMethod = pjp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
                logEntity.setModel(appName + "-" + currentMethod.getName());
            } catch (NoSuchMethodException e) {
                logEntity.setModel("unknow");
            }
        }

        /**
         * 获取入参
         */
        /*Object[] args = pjp.getArgs();
        if (args != null && args.length > 0) {
            StringBuilder inputBuf = new StringBuilder();
            for (int i = 0, let = args.length; i < let; i++) {
                inputBuf.append("args[" + i + "] = " + JSON.toJSONString(args[i]));
                if (i < let - 1) {
                    inputBuf.append(", ");
                }
            }
            logEntity.setInput(inputBuf.toString());
        }*/

        /**
         * 判断出参类型：
         *  1、如果是Exception，说明方法执行抛异常了
         *  1、如果是Result，从Result中获取结果编码，结果描述
         */
        if (o instanceof Exception) {
            logEntity.setResult("异常");
        } else if (o instanceof Result) {
            Result result = (Result) o;
            logEntity.setResult(result.isFlag() ? "成功" : "失败");
        } else {
            logEntity.setResult("-");
        }
        return logEntity;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.appName = environment.getProperty("spring.application.name");
        this.ssoType = environment.getProperty("dragon.boot.sso.type", "");
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

    private String getLocalIp() {
        if (StringUtils.isNotBlank(localIp)) {
            return localIp;
        }
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
            localIp = address.getHostAddress();
        } catch (UnknownHostException e) {
            localIp = "unknow";
        }
        return localIp;
    }
}
