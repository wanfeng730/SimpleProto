package cn.wanfeng.sp.log;

import cn.wanfeng.proto.util.LogUtils;
import cn.wanfeng.sp.SimpleprotoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @date: 2024-06-20 09:33
 * @author: luozh
 * @description: 日志统一工具类测试
 */
@SpringBootTest(classes = SimpleprotoApplication.class)
public class LogUtilsTest {
    @Test
    public void test() {
        LogUtils.debug("LogUtils test debug level success～");
        LogUtils.debug("debug test name={} type={}", "wanfeng", "man");

        LogUtils.info("LogUtils test info level success～");
        LogUtils.info("info test name={} type={}", "wanfeng", "man");
        
        LogUtils.warn("LogUtils test warn level success～");
        LogUtils.warn("warn test name={} type={}", "wanfeng", "man");

        LogUtils.error("LogUtils test error level success～");
        LogUtils.error("error test name={} type={}", "wanfeng", "man");
    }

}
