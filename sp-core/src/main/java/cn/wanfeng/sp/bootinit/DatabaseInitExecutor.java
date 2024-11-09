package cn.wanfeng.sp.bootinit;


import cn.wanfeng.proto.util.LogUtils;
import cn.wanfeng.sp.config.SimpleProtoConfig;
import cn.wanfeng.sp.session.SpSession;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @date: 2024-11-09 16:27
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
@Component
public class DatabaseInitExecutor {

    @Resource
    private SimpleProtoConfig simpleProtoConfig;

    @Resource
    private SpSession spSession;

    @PostConstruct
    public void initDatabaseAndTables() {
        List<String> tableNameList = spSession.databaseStorage().listAllTable(SimpleProtoConfig.currentScheme);
        if(!tableNameList.contains(SimpleProtoConfig.dataTable)){
            spSession.databaseStorage().createDataTable(SimpleProtoConfig.dataTable);
            LogUtils.info("初始化创建对象数据表[{}]", SimpleProtoConfig.dataTable);
        }
        if(!tableNameList.contains(SimpleProtoConfig.settingsTable)){
            spSession.databaseStorage().createSettingsTable(SimpleProtoConfig.settingsTable);
            spSession.databaseStorage().initSettingsTableData(SimpleProtoConfig.settingsTable);
            LogUtils.info("初始化创建设置表[{}]", SimpleProtoConfig.settingsTable);
        }


    }
}
