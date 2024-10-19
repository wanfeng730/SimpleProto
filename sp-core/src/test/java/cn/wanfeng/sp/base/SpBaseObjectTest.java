package cn.wanfeng.sp.base;

import cn.wanfeng.proto.util.staticutils.LogUtils;
import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.base.object.SpBaseObject;
import cn.wanfeng.sp.base.object.SpSession;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @date: 2024-06-21 11:06
 * @author: luozh
 * @description: 基础对象测试
 */
public class SpBaseObjectTest extends SimpleprotoApplicationTest {

    @Resource
    private SpSession spSession;

    @Test
    public void test_findBaseObjectById() {
        SpBaseObject spBaseObject = new SpBaseObject(spSession, 0L);
        Assertions.assertNotNull(spBaseObject);
        LogUtils.info("SpBaseObjectTest测试完成");
    }

    @Test
    public void test_updateBaseObjectById() {
        SpBaseObject spBaseObject = new SpBaseObject(spSession, 0L);

    }
}
