package cn.wanfeng.sp.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @date: 2024-08-31 14:16
 * @author: luozh
 * @description:
 */
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
