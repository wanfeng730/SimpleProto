package cn.wanfeng.sp.test;


import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.api.dataobject.SpUserDO;
import cn.wanfeng.sp.api.domain.SpUser;
import cn.wanfeng.sp.api.mapper.search.UserMapper;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.util.NumberUtils;
import cn.wanfeng.sp.util.ThreadUtils;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @date: 2024-12-15 14:56
 * @author: luozh.wanfeng
 * @since: 1.0
 */
public class UserTest extends SimpleprotoApplicationTest {


    @Resource
    private SpSession session;

    @Resource
    private UserMapper userMapper;

    private static final String USER_NUMBER_INCREASE_NAME = "userNumberIncr";

    @Test
    public void test(){
        Long userNumber = session.increaseLongByName(SimpleProtoConfig.appName + ":" +USER_NUMBER_INCREASE_NAME);
        String numberStr = NumberUtils.generateNumberFixLength(userNumber, 4);
        SpUser spUser = new SpUser(session, "user" + numberStr);
        spUser.store();

        ThreadUtils.sleepQuietly(3000);

        List<SpUserDO> userDOList = userMapper.findAll();
        Assertions.assertTrue(CollectionUtils.isNotEmpty(userDOList));

    }

}
