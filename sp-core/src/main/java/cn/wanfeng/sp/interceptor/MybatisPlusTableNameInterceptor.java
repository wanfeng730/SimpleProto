package cn.wanfeng.sp.interceptor;


import cn.wanfeng.proto.util.LogUtils;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import jakarta.annotation.Resource;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Connection;

/**
 * @date: 2024-10-27 12:53
 * @author: luozh.wanfeng
 * @description: mybatis plus sql修改拦截器 修改表明
 * @since:
 */

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Component
public class MybatisPlusTableNameInterceptor implements Interceptor {

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

    private static final String DATA_TABLE_NAME_PLACEHOLDER = "{data_table}";

    private static final String SETTINGS_TABLE_NAME_PLACEHOLDER = "{setting_table}";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        // 通过MetaObject优雅访问对象的属性，这里是访问statementHandler的属性;：MetaObject是Mybatis提供的一个用于方便、
        // 优雅访问对象属性的对象，通过它可以简化代码、不需要try/catch各种reflect异常，同时它支持对JavaBean、Collection、Map三种类型对象的操作。

        BoundSql boundSql = statementHandler.getBoundSql();
        // 获取到原始sql语句
        String sql = boundSql.getSql();
        // log.info("拦截的sql语句:" + sql);
        // 可以先打印出原始的sql语句，然后根据实际情况修改，我个人建议是下面这样修改
        // 只在原sql上添加条件而不是删除条件
        sql = customProcessSql(sql);
        LogUtils.debug("修改之后的sql：" + sql);
        // 通过反射修改sql语句
        // 下面类似于替换sql
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, sql);
        return invocation.proceed();
    }

    /**
     * 自定义处理sql的方法
     *
     * @param sql 原始sql
     * @return 修改后的sql
     */
    private String customProcessSql(String sql) {
        // dataTable占位符替换
        sql = sql.replace(DATA_TABLE_NAME_PLACEHOLDER, SimpleProtoConfig.dataTable);
        // settingsTable占位符替换
        sql = sql.replace(SETTINGS_TABLE_NAME_PLACEHOLDER, SimpleProtoConfig.settingsTable);
        return sql;
    }
}
