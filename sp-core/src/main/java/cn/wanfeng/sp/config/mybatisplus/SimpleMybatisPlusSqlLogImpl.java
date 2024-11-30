package cn.wanfeng.sp.config.mybatisplus;


import cn.wanfeng.proto.util.LogUtils;
import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @date: 2024-11-30 12:40
 * @author: luozh.wanfeng
 * @description: mybatis sql 打印配置
 * @since: 1.0
 */
@Component
public class SimpleMybatisPlusSqlLogImpl implements Log {

    private static final Logger MYBATIS_PLUS_SQL_LOGGER = LoggerFactory.getLogger("SimpleMybatisPlusSqlLogger");

    public SimpleMybatisPlusSqlLogImpl() {
        LogUtils.info("SimpleProto: Initialized Bean MybatisPlusSqlLogImpl");
    }

    public SimpleMybatisPlusSqlLogImpl(String logFactoryName){
        LogUtils.info("SimpleProto: Initialized Bean MybatisPlusSqlLogImpl[logFactoryName={}]", logFactoryName);
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public void error(String s, Throwable e) {
        MYBATIS_PLUS_SQL_LOGGER.error(s, e);
    }

    @Override
    public void error(String s) {
        MYBATIS_PLUS_SQL_LOGGER.error(s);
    }

    @Override
    public void debug(String s) {
        MYBATIS_PLUS_SQL_LOGGER.debug(s);
    }

    @Override
    public void trace(String s) {
        MYBATIS_PLUS_SQL_LOGGER.trace(s);
    }

    @Override
    public void warn(String s) {
        MYBATIS_PLUS_SQL_LOGGER.warn(s);
    }
}
