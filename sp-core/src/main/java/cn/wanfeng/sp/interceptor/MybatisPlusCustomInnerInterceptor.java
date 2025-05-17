package cn.wanfeng.sp.interceptor;


import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.util.LogUtil;
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
 * @description: mybatis自定义拦截器
 * @since: 1.0
 */
@Component
public class MybatisPlusCustomInnerInterceptor implements InnerInterceptor {

    private static final String DATA_TABLE_NAME_PLACEHOLDER = "{data_table}";

    /**
     * 替换{data_table}为数据表名
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        String sql = boundSql.getSql();
        // 替换表名占位符
        sql = replaceTableName(sql, SimpleProtoConfig.dataTable);

        LogUtil.debug("自定义拦截器最终sql：{}", sql.replace("\n", " "));
        PluginUtils.mpBoundSql(boundSql).sql(sql);
    }

    private static String replaceTableName(String sql, String tableName) {
        return sql.replace(DATA_TABLE_NAME_PLACEHOLDER, tableName);
    }
}
