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

    private static final int DEFAULT_KEEP_ALIVE_SECONDS = 60;

    public static ThreadPoolTaskExecutor createExecutor(int poolSize){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setKeepAliveSeconds(60);
        executor.setQueueCapacity(100);
        executor.setAllowCoreThreadTimeOut(false);
        executor.setPrestartAllCoreThreads(false);
        executor.setStrictEarlyShutdown(false);
        executor.setThreadNamePrefix("SpBaseObjectTest-Async-Task-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

}
