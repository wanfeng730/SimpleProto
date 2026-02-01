package cn.wanfeng.sp.config.redis;


import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.config.custom.SimpleProtoRedisSerializer;
import cn.wanfeng.sp.util.LogUtil;
import jakarta.annotation.Resource;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @date: 2024-11-09 22:48
 * @author: luozh.wanfeng
 * @description: Redis连接配置，使用alibaba的fastjson序列化redis数据，防止乱码
 * @since:
 */
@Configuration
public class RedisConfiguration {

    private static final Logger log = LogUtil.getSimpleProtoLogger();

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

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
        log.info("初始化 >>> RedisTemplate");
        return template;
    }

    // 可选：自定义Lettuce连接工厂（精细控制超时参数）
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(SimpleProtoConfig.redisHost);
        config.setPort(Integer.parseInt(SimpleProtoConfig.redisPort));
        config.setDatabase(Integer.parseInt(SimpleProtoConfig.redisDatabase));
        config.setPassword(RedisPassword.of(SimpleProtoConfig.redisPassword));

        // 连接池配置
        GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(SimpleProtoConfig.redisPoolMaxTotal);          // 对应yml中的max-active
        poolConfig.setMaxIdle(SimpleProtoConfig.redisPoolMaxIdle);           // 对应yml中的max-idle
        poolConfig.setMinIdle(SimpleProtoConfig.redisPoolMinIdle);           // 对应yml中的min-idle
        poolConfig.setMaxWait(Duration.ofMillis(SimpleProtoConfig.redisPoolMaxWait)); // 对应yml中的max-wait
        // 空闲连接最小可驱逐时间
        poolConfig.setMinEvictableIdleDuration(Duration.ofMillis(SimpleProtoConfig.redisPoolMinEvictableIdle));
        // 空闲连接检测周期（毫秒）
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(SimpleProtoConfig.redisPoolTimeBetweenEvictionRuns)); // 60秒检测一次

        // 创建Lettuce连接工厂，指定超时和连接池
        LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(SimpleProtoConfig.redisPoolCommandTimeout))
                .poolConfig(poolConfig)
                .shutdownTimeout(Duration.ofMillis(SimpleProtoConfig.redisPoolShutdownTimeout))
                .build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config, lettucePoolingClientConfiguration);

        return factory;
    }
}
