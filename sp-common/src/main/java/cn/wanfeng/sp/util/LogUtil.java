package cn.wanfeng.sp.util;

import org.apache.commons.lang3.StringUtils;
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

    private static final Map<String, Logger> LOGGER_CACHE = new ConcurrentHashMap<>(512);

    private static final String SimpleProtoLogger = "SimpleProtoLogger";


    public static Logger getCurrentThreadClassLogger() {
        // 从当前堆栈的第4个可以获取到调用日志工具方法的当前类名称
        String className = Thread.currentThread().getStackTrace()[3].getClassName();
        // 以类名作为名称获取logger对象
        return getOrInitLoggerCache(className);
    }

    public static Logger getSimpleProtoLogger() {
        return getOrInitLoggerCache(SimpleProtoLogger);
    }

    protected static Logger getOrInitLoggerCache(String loggerName) {
        if (!LOGGER_CACHE.containsKey(loggerName)) {
            LOGGER_CACHE.put(loggerName, LoggerFactory.getLogger(loggerName));
        }
        return LOGGER_CACHE.get(loggerName);
    }

    protected static String excludePackageClassName(String fullClassName){
        if(StringUtils.isBlank(fullClassName) || !fullClassName.contains(".")){
            return fullClassName;
        }
        int start = fullClassName.lastIndexOf(".") + 1;
        return fullClassName.substring(start);
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
