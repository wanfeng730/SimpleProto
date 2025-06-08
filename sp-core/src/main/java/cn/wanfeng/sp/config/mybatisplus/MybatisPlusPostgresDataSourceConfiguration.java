package cn.wanfeng.sp.config.mybatisplus;


import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.util.LogUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @date: 2024-11-20 23:32
 * @author: luozh.wanfeng
 * @description: Mybatis-plus 数据源动态配置
 * @since: 1.0
 */
@Configuration
@MapperScan(basePackages = "cn.wanfeng.**.mapper.postgres", sqlSessionFactoryRef = "postgresSqlSessionFactory")
public class MybatisPlusPostgresDataSourceConfiguration {

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

    private static final String POSTGRES_MAPPER_LOCATION = "classpath*:mapper/postgres/*.xml";

    @Bean("postgresDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.postgres")
    public DataSource getDataSource() {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();

        dataSource.setInitialSize(SimpleProtoConfig.dataSourceInitialSize);
        dataSource.setMinIdle(SimpleProtoConfig.dataSourceMinIdle);
        dataSource.setMaxActive(SimpleProtoConfig.dataSourceMaxActive);
        // 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(SimpleProtoConfig.dataSourceTimeBetweenEvictionRunsMillis);
        // 连接池中的minIdle数量以内的连接，空闲时间超过minEvictableIdleTimeMillis，则会执行keepAlive操作。实际项目中建议配置成true
        dataSource.setKeepAlive(true);

        dataSource.setValidationQuery("SELECT 1");
        // 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
        dataSource.setTestOnBorrow(false);
        // 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
        dataSource.setTestOnReturn(false);
        return dataSource;
    }

    private static DataSource buildHikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(SimpleProtoConfig.dataSourceUrl);
        config.setUsername(SimpleProtoConfig.dataSourceUsername);
        config.setPassword(SimpleProtoConfig.dataSourcePassword);

        config.setConnectionTestQuery("SELECT 1");          // 连接验证查询
        config.setConnectionTimeout(30000);                 // 连接获取超时30s
        config.setIdleTimeout(240000);                      // 空闲超时4分钟 = 数据库5分钟 * 80%
        config.setMaxLifetime(1800000);                     // 连接最大寿命30分钟
        config.setKeepaliveTime(30000);                     // 每30秒保活检测
        config.addDataSourceProperty("socketTimeout", "30"); // 网络超时30秒

        config.setLeakDetectionThreshold(10000);
        return new HikariDataSource(config);
    }

    @Bean(name = "postgresSqlSessionFactory")
    @Primary
    public SqlSessionFactory postgresSqlSessionFactory(@Qualifier("postgresDataSource") DataSource datasource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        //configuration配置bean
        //MybatisConfiguration configuration = new MybatisConfiguration();
        //configuration.setMapUnderscoreToCamelCase(true);
        //configuration.setCacheEnabled(false);
        // 配置打印sql语句s
        //configuration.setLogImpl(StdOutImpl.class);
        // 添加自定义SQL注入
        //bean.setConfiguration(configuration);

        //拦截器
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //动态表名
        //DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        //可以传多个表名参数，指定哪些表使用MonthTableNameHandler处理表名称
        //dynamicTableNameInnerInterceptor.setTableNameHandler(new MonthTableNameHandler("t_table_name"));
        //以拦截器的方式处理表名称
        //可以传递多个拦截器，即：可以传递多个表名处理器TableNameHandler
        // mybatisPlusInterceptor.addInnerInterceptor(new MybatisPlusTableNameInterceptor());
        //分页插件
        // mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        factoryBean.setDataSource(datasource);
        // 设置mybatis的xml所在位置
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(POSTGRES_MAPPER_LOCATION));
        factoryBean.setPlugins(mybatisPlusInterceptor);
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
        LogUtil.info(" [SimpleProto初始化] Postgres数据源完成");
        return sqlSessionFactory;
    }

    @Bean("postgresSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate postgresSqlSessionTemplate(@Qualifier("postgresSqlSessionFactory") SqlSessionFactory sessionFactory) {
        return new SqlSessionTemplate(sessionFactory);
    }

    /**
     * 事务管理只需要使用数据库的事务管理器即可
     */
    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("postgresDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
