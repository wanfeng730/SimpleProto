package cn.wanfeng.sp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @date: 2024-06-19 11:23
 * @author: luozh
 * @description: 日志统一使用的工具类
 * @since: 1.0
 */

public class LogUtil {

    private static final Map<String, Logger> LOGGER_CACHE = new ConcurrentHashMap<>(128);

    public static Logger getCurrentThreadClassLogger() {
        //从当前堆栈的第4个可以获取到调用日志工具方法的当前类名称
        String className = Thread.currentThread().getStackTrace()[3].getClassName();
        if (!LOGGER_CACHE.containsKey(className)) {
            LOGGER_CACHE.put(className, LoggerFactory.getLogger(className));
        }
        return LOGGER_CACHE.get(className);
    }

    public static void trace(String message){
        Logger logger = getCurrentThreadClassLogger();
        if(logger.isTraceEnabled()){
            logger.trace(message);
        }
    }

    public static void trace(String message, Object... vars) {
        Logger logger = getCurrentThreadClassLogger();
        if (logger.isTraceEnabled()) {
            logger.trace(message, vars);
        }
    }

    public static void debug(String message) {
        Logger logger = getCurrentThreadClassLogger();
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public static void debug(String message, Object... vars) {
        Logger logger = getCurrentThreadClassLogger();
        if (logger.isDebugEnabled()) {
            logger.debug(message, vars);
        }
    }

    public static void info(String message) {
        Logger logger = getCurrentThreadClassLogger();
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public static void info(String message, Object... vars) {
        Logger logger = getCurrentThreadClassLogger();
        if (logger.isInfoEnabled()) {
            logger.info(message, vars);
        }
    }

    public static void warn(String message) {
        Logger logger = getCurrentThreadClassLogger();
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    public static void warn(String message, Object... vars) {
        Logger logger = getCurrentThreadClassLogger();
        if (logger.isWarnEnabled()) {
            logger.warn(message, vars);
        }
    }

    public static void error(String message) {
        Logger logger = getCurrentThreadClassLogger();
        if (logger.isErrorEnabled()) {
            logger.error(message);
        }
    }

    public static void error(String message, Object... vars) {
        Logger logger = getCurrentThreadClassLogger();
        if (logger.isErrorEnabled()) {
            logger.error(message, vars);
        }
    }

}
