package cn.wanfeng.sp.config;


import cn.wanfeng.sp.interceptor.MybatisPlusTableNameInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @date: 2024-10-22 23:06
 * @author: luozh.wanfeng
 * @description: Mybatis拦截器配置
 * @since: 1.0
 */
@Configuration
public class MybatisPlusConfiguration {

    @Bean
    public MybatisPlusTableNameInterceptor mybatisPlusInterceptor() {
        return new MybatisPlusTableNameInterceptor();
    }
}
