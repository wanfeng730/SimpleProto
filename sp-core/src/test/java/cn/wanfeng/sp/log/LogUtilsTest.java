package cn.wanfeng.sp.log;

import cn.wanfeng.proto.util.LogUtils;
import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.session.SpSession;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @date: 2024-06-20 09:33
 * @author: luozh
 * @description: 日志统一工具类测试
 */
public class LogUtilsTest extends SimpleprotoApplicationTest {

    @Resource
    private SpSession spSession;

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

    @Test
    public void testPostgresqlAutoCreateTable(){
        List<String> tableNameList = spSession.databaseStorage().listAllTable(SimpleProtoConfig.currentScheme);
        Assertions.assertTrue(tableNameList.contains(SimpleProtoConfig.dataTable));
        Assertions.assertTrue(tableNameList.contains(SimpleProtoConfig.settingsTable));
    }

}
