package cn.wanfeng.sp.redis;


import cn.wanfeng.sp.SimpleprotoApplicationTest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @date: 2024-11-09 22:39
 * @author: luozh.wanfeng
 */
public class RedisTest extends SimpleprotoApplicationTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void test(){
        redisTemplate.opsForValue().set("name", "wanfeng730");
        String name = (String) redisTemplate.opsForValue().get("name");
        Assertions.assertEquals("wanfeng730", name);

        redisTemplate.delete("name");
        name = (String) redisTemplate.opsForValue().get("name");
        Assertions.assertNull(name);
    }
}
