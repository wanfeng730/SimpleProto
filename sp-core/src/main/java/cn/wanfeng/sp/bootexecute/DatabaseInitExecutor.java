package cn.wanfeng.sp.bootexecute;

import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.session.SpSession;
import cn.wanfeng.sp.util.LogUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @date: 2024-11-09 16:27
 * @author: luozh.wanfeng
 * @description: 数据库启动初始化
 * @since: 1.0
 */
@Component
public class DatabaseInitExecutor {

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

    @Resource
    private SpSession spSession;

    private static final Logger log = LogUtil.getSimpleProtoLogger();

    @PostConstruct
    public void initDatabaseAndTables() {
        List<String> tableNameList = spSession.databaseStorage().listAllTable(SimpleProtoConfig.currentScheme);
        if(!tableNameList.contains(SimpleProtoConfig.dataTable)){
            spSession.databaseStorage().createDataTable(SimpleProtoConfig.dataTable);
            log.info("初始化 创建对象数据表[{}]", SimpleProtoConfig.dataTable);
        }
        if(!tableNameList.contains(SimpleProtoConfig.settingsTable)){
            spSession.databaseStorage().createSettingsTable(SimpleProtoConfig.settingsTable);
            spSession.databaseStorage().initSettingsTableData(SimpleProtoConfig.settingsTable);
            log.info("初始化 创建设置表[{}]", SimpleProtoConfig.settingsTable);
        }
        log.info("初始化 数据库存储表完成");
    }
}
