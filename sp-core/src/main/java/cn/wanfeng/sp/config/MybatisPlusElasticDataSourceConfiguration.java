package cn.wanfeng.sp.config;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @date: 2024-11-20 23:32
 * @author: luozh.wanfeng
 * @description: Mybatis-plus 数据源动态配置
 * @since:
 */
@Configuration
@MapperScan(basePackages = "cn.wanfeng.**.mapper.es", sqlSessionFactoryRef = "elasticSqlSessionFactory")
public class MybatisPlusElasticDataSourceConfiguration {

    private static final String ELASTIC_MAPPER_LOCATION = "classpath*:mapper/es/*.xml";

    @Bean("elasticDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.elastic")
    public DataSource getDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "elasticSqlSessionFactory")
    public SqlSessionFactory db1SqlSessionFactory(@Qualifier("elasticDataSource") DataSource datasource) throws Exception {
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
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(ELASTIC_MAPPER_LOCATION));
        factoryBean.setPlugins(mybatisPlusInterceptor);
        return factoryBean.getObject();
    }

    @Bean("elasticSqlSessionTemplate")
    public SqlSessionTemplate db1SqlSessionTemplate(@Qualifier("elasticSqlSessionFactory") SqlSessionFactory sessionFactory) {
        return new SqlSessionTemplate(sessionFactory);
    }

    @Bean("elasticTransactionManager")
    public PlatformTransactionManager db1TransactionManager(@Qualifier("elasticDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
