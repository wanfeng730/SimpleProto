package cn.wanfeng.sp.base;

import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.base.object.SpBaseObject;
import cn.wanfeng.sp.base.object.SpSession;
import cn.wanfeng.sp.util.LogUtils;
import jakarta.annotation.Resource;
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
    public void test1() {
        SpBaseObject spBaseObject = new SpBaseObject(spSession, 0L);
        LogUtils.info("SpBaseObjectTest测试完成");
    }
}
