package cn.wanfeng.sp.interceptor;


import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * @date: 2024-11-30 12:48
 * @author: luozh.wanfeng
 * @description: mybatis表名替换拦截器
 * @since: 1.0
 */
@Component
public class MybatisPlusTableNameInnerInterceptor implements InnerInterceptor {

    private static final String DATA_TABLE_NAME_PLACEHOLDER = "{data_table}";

    /**
     * 替换{data_table}为数据表名
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        String sql = boundSql.getSql();
        sql = sql.replace(DATA_TABLE_NAME_PLACEHOLDER, SimpleProtoConfig.dataTable);
        PluginUtils.mpBoundSql(boundSql).sql(sql);
    }
}
