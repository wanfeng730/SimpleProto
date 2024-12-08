package cn.wanfeng.sp.log;

import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.util.LogUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @date: 2024-06-20 09:33
 * @author: luozh
 * @description: 日志统一工具类测试
 */
public class LogUtilTest extends SimpleprotoApplicationTest {

    @Resource
    private SpSession spSession;

    @Test
    public void test() {
        LogUtil.debug("LogUtil test debug level success～");
        LogUtil.debug("debug test name={} type={}", "wanfeng", "man");

        LogUtil.info("LogUtil test info level success～");
        LogUtil.info("info test name={} type={}", "wanfeng", "man");
        
        LogUtil.warn("LogUtil test warn level success～");
        LogUtil.warn("warn test name={} type={}", "wanfeng", "man");

        LogUtil.error("LogUtil test error level success～");
        LogUtil.error("error test name={} type={}", "wanfeng", "man");
    }

    @Test
    public void testPostgresqlAutoCreateTable(){
        List<String> tableNameList = spSession.databaseStorage().listAllTable(SimpleProtoConfig.currentScheme);
        Assertions.assertTrue(tableNameList.contains(SimpleProtoConfig.dataTable));
        Assertions.assertTrue(tableNameList.contains(SimpleProtoConfig.settingsTable));
    }

}
