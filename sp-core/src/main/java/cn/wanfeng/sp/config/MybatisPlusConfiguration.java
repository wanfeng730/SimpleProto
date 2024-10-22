package cn.wanfeng.sp.config;


import cn.wanfeng.proto.util.LogUtils;
import cn.wanfeng.sp.interceptor.MybatisDynamicTableNameInterceptor;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${dynamicSql.settingsTable}")
    private String settingsTable;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        LogUtils.info("生成mybatis-plus拦截器");

        interceptor.addInnerInterceptor(new MybatisDynamicTableNameInterceptor(settingsTable));

        // 添加分页插件
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();
        // 设置请求的页面大于最大页后操作，true调回到首页，false继续请求。默认false
        pageInterceptor.setOverflow(false);
        // 单页分页条数限制，默认无限制
        pageInterceptor.setMaxLimit(10000L);
        // 设置数据库类型
        pageInterceptor.setDbType(DbType.MYSQL);

        interceptor.addInnerInterceptor(pageInterceptor);
        return interceptor;
    }
}
