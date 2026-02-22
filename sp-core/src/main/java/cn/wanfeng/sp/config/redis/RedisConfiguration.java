package cn.wanfeng.sp.config.redis;


import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.util.LogUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    /**
     * 手动创建RedissonClient Bean（单机模式）
     * 该Bean会被Spring容器管理，可直接注入使用
     */
    @Bean(destroyMethod = "shutdown") // 容器销毁时自动关闭RedissonClient
    public RedissonClient redissonClient() {
        if(StringUtils.isBlank(SimpleProtoConfig.redisPassword)){
            throw new SpException("未配置Redis连接密码");
        }

        try {
            // 创建Redisson配置对象
            Config config = new Config();

            // 配置单机Redis连接
            String redisAddress = String.format("redis://%s:%s", SimpleProtoConfig.redisHost, SimpleProtoConfig.redisPort);
            config.useSingleServer()
                    .setAddress(redisAddress)
                    .setUsername(SimpleProtoConfig.redisUsername)
                    .setPassword(SimpleProtoConfig.redisPassword)
                    .setDatabase(SimpleProtoConfig.redisDatabase)
                    .setConnectionPoolSize(SimpleProtoConfig.redisPoolMaxSize) // 连接池最大连接数
                    .setConnectionMinimumIdleSize(SimpleProtoConfig.redisPoolMinIdle) // 最小空闲连接数
                    .setTimeout(SimpleProtoConfig.redisPoolConnectTimeout) // 连接超时时间
                    .setIdleConnectionTimeout(SimpleProtoConfig.redisPoolIdleConnectionTimeout) // 空闲连接超时时间（毫秒）
                    .setPingConnectionInterval(SimpleProtoConfig.redisPoolPingConnectionInterval); // 心跳检测间隔（毫秒）

            RedissonClient redissonClient = Redisson.create(config);
            log.info("初始化 >>> RedissionClient客户端 当前Key数量：{}", redissonClient.getKeys().count());
            // @luozh-code: 打印连接池信息


            return redissonClient;
        } catch (Exception e) {
            log.error("初始化RedissionClient客户端失败", e);
            throw new SpException(e, "初始化RedissionClient客户端失败");
        }
    }
}
