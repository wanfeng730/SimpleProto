package cn.wanfeng.sp.test;

import cn.wanfeng.sp.api.domain.SpUser;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.util.NumberUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * BulkOperatorTest: desc.
 *
 * @date: 2025-04-01 17:49
 * @author: luozh.wanfeng
 */
public class BulkOperatorTest extends SimpleApplicationContextTest{

    @Resource
    private SpSession session;

    private static final String USER_NUMBER_INCREASE_NAME = "userNumberIncr";

    @Test
    public void test(){
        Long userNumber = session.increaseLongByName(SimpleProtoConfig.appName + ":" + USER_NUMBER_INCREASE_NAME);
        String numberStr = NumberUtils.generateNumberFixLength(userNumber, 4);
        SpUser spUser = new SpUser(session, "user" + numberStr);
        spUser.store();
    }

}
