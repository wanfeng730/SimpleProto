package cn.wanfeng.sp.config;


import com.amazon.opendistroforelasticsearch.jdbc.ElasticsearchDataSource;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @date: 2024-11-20 23:32
 * @author: luozh.wanfeng
 * @description: Mybatis-plus 数据源动态配置
 * @since:
 */
@Configuration
@MapperScan(basePackages = "cn.wanfeng.**.mapper.es", sqlSessionFactoryRef = "searchSqlSessionFactory")
public class MybatisPlusSearchDataSourceConfiguration {

    private static final String ELASTIC_MAPPER_LOCATION = "classpath*:mapper/es/*.xml";

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

    @Bean("searchDataSource")
    public DataSource getDataSource() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("useSSL", SimpleProtoConfig.esJdbcUseSSL);
        properties.setProperty("user", SimpleProtoConfig.esUsername);
        properties.setProperty("password", SimpleProtoConfig.esPassword);

        ElasticsearchDataSource dataSource = new ElasticsearchDataSource();
        dataSource.setUrl(SimpleProtoConfig.esJdbcUrl);
        dataSource.setProperties(properties);
        return dataSource;
    }

    @Bean(name = "searchSqlSessionFactory")
    public SqlSessionFactory db1SqlSessionFactory(@Qualifier("searchDataSource") DataSource datasource) throws Exception {
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

    @Bean("searchSqlSessionTemplate")
    public SqlSessionTemplate elasticSqlSessionTemplate(@Qualifier("searchSqlSessionFactory") SqlSessionFactory sessionFactory) {
        return new SqlSessionTemplate(sessionFactory);
    }

}
