package cn.wanfeng.sp.util;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @date: 2024-11-10 23:42
 * @author: luozh.wanfeng
 * @description: 线程池执行器根据模版生成的工具类
 * @since: 1.0
 */
public class ThreadPoolTemplateUtils {

    public static ThreadPoolTaskExecutor createDefaultThreadPool(int poolSize){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setKeepAliveSeconds(60);
        executor.setQueueCapacity(100);
        executor.setAllowCoreThreadTimeOut(false);
        executor.setThreadNamePrefix("Untitled-Async-Task-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // JDK 21
        executor.setPrestartAllCoreThreads(false);
        // JDK 21
        executor.setStrictEarlyShutdown(false);
        return executor;
    }

    public static ThreadPoolTaskExecutor initThreadPoolByPrefixName(String prefixName){
        ThreadPoolTaskExecutor taskExecutor = createDefaultThreadPool(4);
        taskExecutor.setThreadNamePrefix(prefixName);
        taskExecutor.initialize();
        return taskExecutor;
    }

}
