package cn.wanfeng.sp.redis;


import cn.wanfeng.sp.util.LogUtils;
import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.cache.CacheOperator;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * @date: 2024-11-09 22:39
 * @author: luozh.wanfeng
 */
public class CacheOperatorTest extends SimpleprotoApplicationTest {

    @Resource
    private CacheOperator cacheOperator;

    private static final String KEY = "Wanfengkey";

    @Test
    public void test(){
        String key = KEY + System.currentTimeMillis();

        cacheOperator.set(key, "wanfeng");
        Assertions.assertEquals("wanfeng", cacheOperator.get(key));
        cacheOperator.del(key);

        cacheOperator.incr(key);
        Assertions.assertEquals(1, cacheOperator.get(key));
        cacheOperator.incr(key, 30);
        Assertions.assertEquals(31, cacheOperator.get(key));
        cacheOperator.del(key);


        cacheOperator.decr(key);
        Assertions.assertEquals(-1, cacheOperator.get(key));
        cacheOperator.decr(key, 50);
        Assertions.assertEquals(-51, cacheOperator.get(key));
        cacheOperator.del(key);

        cacheOperator.listLeftPush(key, 1);
        cacheOperator.listLeftPush(key, "wanfeng");
        Assertions.assertEquals("wanfeng", cacheOperator.listLeftPop(key));
        Assertions.assertEquals(1, cacheOperator.listLeftPop(key));

        cacheOperator.listRightPush(key, 1);
        cacheOperator.listRightPush(key, "wanfeng");
        Assertions.assertEquals("wanfeng", cacheOperator.listRightPop(key));
        Assertions.assertEquals(1, cacheOperator.listRightPop(key));

        cacheOperator.setAdd(key, "wanfeng");
        cacheOperator.setAdd(key, 730);
        Assertions.assertTrue(cacheOperator.isMember(key, "wanfeng"));
        Assertions.assertTrue(cacheOperator.isMember(key, 730));
        Assertions.assertFalse(cacheOperator.isMember(key, 404));
        Set<Object> set1 = cacheOperator.setMembers(key);
        Assertions.assertTrue(CollectionUtils.isNotEmpty(set1) && set1.size() == 2);
        cacheOperator.setRemove(key, "wanfeng");
        cacheOperator.setRemove(key, 730);

        cacheOperator.zsetAdd(key, "wanfeng", 4.56);
        cacheOperator.zsetAdd(key, "luozh", 5.98);
        cacheOperator.zsetAdd(key, "730", 1.00);
        Assertions.assertEquals(0, cacheOperator.zsetRankAsc(key, "730"));
        Assertions.assertEquals(2, cacheOperator.zsetRankDesc(key, "730"));
        Set<Object> rangeByIndex = cacheOperator.zsetRangeByIndex(key, 0, 1);
        Assertions.assertEquals(2, rangeByIndex.size());
        Set<Object> rangeByScore = cacheOperator.zsetRangeByScore(key, 3, 5);
        Assertions.assertEquals(1, rangeByScore.size());
        cacheOperator.zsetIncrScore(key, "wanfeng", 100);
        Long count = cacheOperator.zsetCountByScore(key, 3, 5);
        Assertions.assertEquals(0, count);
        cacheOperator.zsetRemoveByIndex(key, 0, 0);
        cacheOperator.zsetRemoveByScore(key, 5, 105);

        Assertions.assertTrue(cacheOperator.lock("testLock-YJ"));
        Assertions.assertFalse(cacheOperator.lock("testLock-YJ"));
        cacheOperator.unLock("testLock-YJ");
        Assertions.assertTrue(cacheOperator.lock("testLock-YJ"));
        cacheOperator.unLock("testLock-YJ");

        Assertions.assertTrue(cacheOperator.lock("testLock-EXPIRE", 10));
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(cacheOperator.lock("testLock-EXPIRE", 10));


        LogUtils.info("CacheOperator测试完成");
    }
}
