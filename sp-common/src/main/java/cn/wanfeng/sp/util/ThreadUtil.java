package cn.wanfeng.sp.util;


/**
 * @date: 2024-12-15 22:10
 * @author: luozh.wanfeng
 * @description: 线程工具类
 * @since: 1.0
 */
public class ThreadUtil {

    public static void sleepQuietly(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LogUtil.error("Thread Sleep InterruptedException, Reason:", e);
        }
    }

}
