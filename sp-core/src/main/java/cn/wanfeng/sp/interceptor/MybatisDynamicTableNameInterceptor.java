package cn.wanfeng.sp.interceptor;


import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @date: 2024-10-22 23:10
 * @author: luozh.wanfeng
 * @description: mybatis动态表名拦截器
 * @since: 1.0
 */
public class MybatisDynamicTableNameInterceptor implements InnerInterceptor {

    @Value("${dynamicSql.settingsTable}")
    private String settingsTable;

    private static final String SETTINGS_TABLE_NAME_PLACEHOLDER = "{setting_table}";

    public MybatisDynamicTableNameInterceptor(String settingsTable) {
        this.settingsTable = settingsTable;
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        // 原始sql：SELECT  id,name,age,address  FROM t_user WHERE (name = ? AND age = ?)
        String originSql = boundSql.getSql();
        System.out.println("originSql：" + originSql);

        // 修改后的sql
        String targetSql = originSql.replace(SETTINGS_TABLE_NAME_PLACEHOLDER, settingsTable);

        // 修改完成的sql 再设置回去
        PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
        mpBoundSql.sql(targetSql);
    }

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        InnerInterceptor.super.beforeUpdate(executor, ms, parameter);
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        InnerInterceptor.super.beforePrepare(sh, connection, transactionTimeout);
    }

    @Override
    public void beforeGetBoundSql(StatementHandler sh) {
        InnerInterceptor.super.beforeGetBoundSql(sh);
    }
}
