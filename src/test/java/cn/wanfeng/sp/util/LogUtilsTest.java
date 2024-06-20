package cn.wanfeng.sp.util;

import cn.wanfeng.sp.SimpleprotoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @date: 2024-06-20 09:33
 * @author: luozh
 * @description:
 * @since:
 */
@SpringBootTest(classes = SimpleprotoApplication.class)
public class LogUtilsTest {
    @Test
    public void test() {
        LogUtils.info("LogUtils test success～");
    }

}
