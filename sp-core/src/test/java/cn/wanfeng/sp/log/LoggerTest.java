package cn.wanfeng.sp.log;

import cn.wanfeng.sp.SimpleprotoApplication;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @date: 2024-08-31 14:16
 * @author: luozh
 * @description:
 */
@SpringBootTest(classes = SimpleprotoApplication.class)
public class LoggerTest {

    @Test
    public void testLogback() {
        Logger logger = LoggerFactory.getLogger("spLogger");
        logger.info("123123123");
        logger.info("123123123");
        logger.info("123123123");
        logger.info("123123123");
        logger.info("123123123");
        logger.info("123123123");
    }


}
