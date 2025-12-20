package cn.wanfeng.sp.config.mybatisplus;


import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.util.LogUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private static final Logger log = LogUtil.getSimpleProtoLogger();

    private static final String POSTGRES_MAPPER_LOCATION = "classpath*:mapper/postgres/*.xml";


    @Bean("postgresDataSource")
    public DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(SimpleProtoConfig.dataSourceDriver);
        config.setJdbcUrl(SimpleProtoConfig.dataSourceUrl);
        config.setUsername(SimpleProtoConfig.dataSourceUsername);
        config.setPassword(SimpleProtoConfig.dataSourcePassword);
        // size...
        config.setMaximumPoolSize(SimpleProtoConfig.dataSourceMaximumPoolSize);
        config.setMinimumIdle(SimpleProtoConfig.dataSourceMinimumIdle);

        config.setConnectionTimeout(SimpleProtoConfig.dataSourceConnectionTimeout);
        config.setMaxLifetime(SimpleProtoConfig.dataSourceMaxLifetime);
        config.setIdleTimeout(SimpleProtoConfig.dataSourceIdleTimeout);

        config.setKeepaliveTime(SimpleProtoConfig.dataSourceKeepAliveTime);
        config.setConnectionTestQuery(SimpleProtoConfig.dataSourceConnectionTestQuery);
        config.setValidationTimeout(SimpleProtoConfig.dataSourceValidationTimeout);

        config.setLeakDetectionThreshold(SimpleProtoConfig.dataSourceLeakDetectionThreshold);

        log.info("初始化 >>> Postgres Hikari连接池配置\n    ConnectionTimeout: {}\n    IdleTimeout: {}\n    KeepAliveTime: {}\n    ValidationTimeout: {}\n    LeakDetectionThreshold: {}",
                config.getConnectionTimeout(), config.getIdleTimeout(), config.getKeepaliveTime(), config.getValidationTimeout(), config.getLeakDetectionThreshold());
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
        // 分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));

        factoryBean.setDataSource(datasource);
        // 设置mybatis的xml所在位置
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(POSTGRES_MAPPER_LOCATION));
        factoryBean.setPlugins(mybatisPlusInterceptor);
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
        log.info("初始化 >>> Postgres数据源");


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
