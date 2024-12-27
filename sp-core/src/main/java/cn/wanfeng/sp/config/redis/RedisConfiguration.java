package cn.wanfeng.sp.config.redis;


import cn.wanfeng.sp.config.custom.SimpleProtoRedisSerializer;
import cn.wanfeng.sp.util.LogUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @date: 2024-11-09 22:48
 * @author: luozh.wanfeng
 * @description: Redis连接配置，使用alibaba的fastjson序列化redis数据，防止乱码
 * @since:
 */
@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        SimpleProtoRedisSerializer<Object> redisSerializer = new SimpleProtoRedisSerializer<>(Object.class);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(redisSerializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(redisSerializer);

        template.afterPropertiesSet();
        LogUtil.info("【初始化】RedisTemplate完成");
        return template;
    }

}
