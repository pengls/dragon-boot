/**
 * 
 */
package com.dragon.boot.log;


import com.dragon.boot.log.model.BusLog;

/**
 * @ClassName: LogContext
 * @Author: pengl
 * @Date: 2019-05-15 15:05
 * @Description: 本地线程获取日志数据
 * @Version: 1.0
 **/
public class LogContext {
	private LogContext(){}

	private static final ThreadLocal<BusLog> contextHolder = new ThreadLocal<>();

	public static void set(BusLog busLogBean) {
		contextHolder.set(busLogBean);
	}

	public static BusLog get() {
		return contextHolder.get();
	}

	public static void clear() {
		contextHolder.remove();
	}
}
